# Dockerize a Spring Boot application using Jib

mvn compile jib:dockerBuild

#Build an image tarball

mvn compile jib:buildTar

#This builds and saves your image to target/jib-image.tar, which you can load into docker with:

docker load --input target/jib-image.tar