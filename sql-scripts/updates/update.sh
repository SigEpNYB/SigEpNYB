#!/bin/bash

cd sql-scripts/updates

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

version=$(mysql -u builder -D fratdata -e "SELECT version FROM version" -s)
if [ $? != "0" ]
then
    echo "No version table"
    echo "Running reset script"
    cd ..
    ./reset.sh
    echo -e "${GREEN}Finished.${NC}"
    exit 0
fi
echo "Your database is currently at version $version"

maxVersion=1
for f in *.sql
do
    v=${f:2:1}
    if [ $v -gt $maxVersion ]
    then
        maxVersion=$v
    fi
done

echo "Your database should be at version $maxVersion"

if [ $version -gt $maxVersion ]
then
    echo "You seem to be missing some update scripts"
    echo -e "${RED}Aborting.${NC}"
    exit 1
fi

if [ $version -eq $maxVersion ]
then
    echo -e "${GREEN}Your database is already up-to-date.${NC}"
    exit 0
fi

let version=$version+1
for v in `seq $version $maxVersion`
do
    echo "Updating to version $v..."
    mysql -u builder -D fratdata < to$v.sql
    status=$?
    if [ $status != "0" ]
    then
        echo "Error updating"
        echo -e "${RED}Aborting.${NC}"
        exit $status
    fi
    
    mysql -u builder -D fratdata -e "UPDATE version SET version = $v"
done

echo -e "${GREEN}Updates complete.${NC}"
