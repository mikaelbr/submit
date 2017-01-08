#!/usr/bin/env bash


BASEDIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

# resolve symlinks
while [ -h "$BASEDIR/$0" ]; do
    DIR=$(dirname -- "$BASEDIR/$0")
    SYM=$(readlink $BASEDIR/$0)
    BASEDIR=$(cd $DIR && cd $(dirname -- "$SYM") && pwd)
done
cd ${BASEDIR}

ENVS=$(eb list --profile javabin | sed 's/^\* //')

if [[ ${1} != submit-* ]]; then
  echo "Usage: ${0} submit-<environment>"
  exit 1
elif [ $(echo "$ENVS" | grep "^$1$" -c) -eq 0 ]; then
  echo "Environment not recognized: '$1'. Use one of the following:"
  echo
  echo "$ENVS"
  exit 1
fi

BEANSTALK_ENV=$1
ENV="$(echo $BEANSTALK_ENV | cut -d '-' -f 2)"

echo "> preparing secret properties"

ansible-vault view "secretconfig/application-$ENV.properties.encrypted" >> "application-$ENV.properties"

git add "application-$ENV.properties"

echo "> Starting deploy"
eb deploy "$BEANSTALK_ENV" --staged --profile javabin

git rm -f "application-$ENV.properties"

echo "Deploy complete."
