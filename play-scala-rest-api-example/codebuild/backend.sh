#!/bin/bash -ex
#
# build and test backend
#


DOCKER_NW=${DOCKER_NW:-"docker_default"}
DOCKER_POSTGRES_IMAGE=${DOCKER_POSTGRES_IMAGE:-"postgres:10.6-alpine"}
DB_HOST=${DB_HOST:-"postgres"}
DB_NAME=${DB_NAME:-"prf_api_ut"}
DB_USER=${DB_USER:-"postgres"}

export CODEBUILD_GIT_BRANCH="$(git symbolic-ref HEAD --short 2>/dev/null)"
if [ "$CODEBUILD_GIT_BRANCH" = "" ] ; then
  CODEBUILD_GIT_BRANCH="$(git branch -a --contains HEAD | sed -n 2p | awk '{ printf $1 }')";
  export CODEBUILD_GIT_BRANCH=${CODEBUILD_GIT_BRANCH#remotes/origin/};
fi

function shouldTriggerBuild(){
	[[ $CODEBUILD_GIT_BRANCH == *"/be/"* ]]

}
#
# install java (only print version)
#
function install_java() {
    if shouldTriggerBuild; then
        echo "Java version: $(java -version)"
    else
       echo "skip install_java, not target"
    fi
}

#
# Download salmonflake lib jar from s3
#
function pre_build_deps() {
    if shouldTriggerBuild; then
       echo "pre_build_deps"
    else
       echo "skip pre_build_deps, not target"
    fi
 }

#
# Run docker containers for unittest
#
function pre_build_run_docker() {
    if shouldTriggerBuild; then
       echo "pre_build_flyway"
    else
       echo "skip pre_build_run_docker, not target"
    fi
}

#
# Run flyway migration before run sbt test
#
function pre_build_flyway() {
    if shouldTriggerBuild; then
       echo "pre_build_flyway"
    else
       echo "skip pre_build_flyway, not target"
    fi
}

#
# build sbt project and run tests, send coverage report to Codecov
#
function build() {
    if shouldTriggerBuild; then
        cd "$CODEBUILD_SRC_DIR"
        cd play-scala-rest-api-example
        ./sbt test
    else
    	echo "skip build, not target"
    fi
}

#
# Upload coverage report to codecov.io
#
function upload_coverage_report() {
    if shouldTriggerBuild; then
        echo "upload_coverage_report"
    else
        echo "skip upload_coverage_report, not target"
    fi
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
