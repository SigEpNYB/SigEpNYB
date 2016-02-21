var gulp = require('gulp');
var jade = require('gulp-jade');
var sass = require('gulp-sass');
var uglify = require('gulp-uglify');

gulp.task('jade', function() {
  gulp.src('./web/jade/*')
    .pipe(jade({pretty: true}))
    .pipe(gulp.dest('./WebContent'));
});

gulp.task('sass', function() {
  gulp.src('./web/sass/*')
    .pipe(sass())
    .pipe(gulp.dest('./WebContent/css'))
});

gulp.task('js', function() {
  gulp.src('./web/js/*')
    .pipe(gulp.dest('./WebContent/js'))
});

gulp.task('development', ['jade', 'sass', 'js']);

gulp.task('jade-production', function() {
  gulp.src('./web/jade/*')
    .pipe(jade({pretty: false}))
    .pipe(gulp.dest('./WebContent'));
});

gulp.task('sass-production', function() {
  gulp.src('./web/sass/*')
    .pipe(sass({outputStyle: 'compressed'}))
    .pipe(gulp.dest('./WebContent/css'));
});

gulp.task('js-production', function() {
  gulp.src('./web/js/*')
    .pipe(uglify())
    .pipe(gulp.dest('./WebContent/js'));
});

gulp.task('production', ['jade-production', 'sass-production',
  'js-production']);

gulp.task('default', ['development']);