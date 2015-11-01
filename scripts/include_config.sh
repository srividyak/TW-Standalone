#!/bin/bash

if [ $# -ne 2 ]; then
  echo "usage: include_config.sh <project_name> zip | tar"
  exit 1
fi

project_name=$1
format=$2
dist_dir=build/distributions
base_dir=$(dirname $0)
cd ${dist_dir}
if [ "$format" = "tar" ]
then
  tar -xvf ${project_name}.tar
  rm ${project_name}.tar
else
  unzip ${project_name}.zip
  rm ${project_name}.zip
fi
cp -r ../../config $project_name/

if [ "$format" = "tar" ]
then
  tar -cvf ${project_name}.tar ${project_name}
else
  zip -r ${project_name}.zip ${project_name}
fi

rm -rf ${project_name}
