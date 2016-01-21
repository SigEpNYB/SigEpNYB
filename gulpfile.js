var gulp = require('gulp');
var browserify = require('browserify');

gulp.task('jade', function() {
  gulp.src('./WebContent/jade/*')
    .pipe(jade())
    .pipe(gulp.dest('./WebContent'));
});

gulp.task('sass', function() {
  gulp.src('./WebContent/sass/*')
})

gulp.task('default', ['jade'])