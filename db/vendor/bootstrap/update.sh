make bootstrap
cp bootstrap/css/*.min.css ../../app/assets/stylesheets
cp bootstrap/js/bootstrap.min.js ../../app/assets/javascripts
cp bootstrap/img/*.png ../../app/assets/images
rm -rf bootstrap
