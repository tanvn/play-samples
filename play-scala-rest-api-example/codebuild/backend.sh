#!/bin/bash -ex
#
# build and test backend
#


DOCKER_NW=${DOCKER_NW:-"docker_default"}
DOCKER_POSTGRES_IMAGE=${DOCKER_POSTGRES_IMAGE:-"postgres:10.6-alpine"}
DB_HOST=${DB_HOST:-"postgres"}
DB_NAME=${DB_NAME:-"prf_api_ut"}
DB_USER=${DB_USER:-"postgres"}


#
# install java (only print version)
#
function install_java() {
    echo "Java version: $(java -version)"
}

#
# Download salmonflake lib jar from s3
#
function pre_build_deps() {
    echo "pre_build_deps"
}

#
# Run docker containers for unittest
#
function pre_build_run_docker() {
    echo "pre_build_run_docker"
}

#
# Run flyway migration before run sbt test
#
function pre_build_flyway() {
    echo "pre_build_flyway"
}

#
# build sbt project and run tests, send coverage report to Codecov
#
function build() {
    cd "$CODEBUILD_SRC_DIR"
    cd play-scala-rest-api-example
    ./sbt test
}

#
# Upload coverage report to codecov.io
#
function upload_coverage_report() {
    echo "upload_coverage_report"
}


BUILD_PHASE="$1"
echo "Phase is $BUILD_PHASE"

case "$BUILD_PHASE" in
    install)
        install_java
        ;;
    pre_build)
        pre_build_deps
        pre_build_run_docker
        pre_build_flyway
        ;;
    build)
        build
        ;;
    post_build)
        upload_coverage_report
        ;;
    *)
        echo "Phase $BUILD_PHASE is not supported."
        exit 1
        ;;
esac

exit 0
