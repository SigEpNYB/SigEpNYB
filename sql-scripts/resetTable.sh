#!/bin/bash
echo "Enter Password: "
read -s password
mysql -u root -p$password --database=fratdata --execute="drop table $1"
mysql -u root -p$password --database=fratdata < create-$1.sql

populate=populate-$1.sql
if [ -e $populate ] 
then
    mysql -u root -p$password --database=fratdata < $populate
fi
