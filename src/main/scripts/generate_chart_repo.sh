#!/usr/bin/env bash

set -e

# The directory, relative to the git repository root, where the Helm charts are stored
CHARTS_SRC_DIR="src/main/charts"

# The directory that will contain the generated chart repo files
# "docs" is the hard-coded directory used by GitHub-Pages (yeah, I know)
PUBLISH_DIR="docs"

PACKAGE_DIR="target/helm"

TAG_VERSION=$1
GITHUB_TOKEN=$2

if [[ -z $TAG_VERSION ]]
then
  echo "Must supply a version string"
  exit 1
fi

if [[ -z $GITHUB_TOKEN ]]
then
  echo "Must supply a github token"
  exit 1
fi

rm -rf "$PACKAGE_DIR"

for chart in "$CHARTS_SRC_DIR"/*
  do
    echo "Packaging chart $chart with version $TAG_VERSION"
    helm package "$chart" --version "$TAG_VERSION" --destination "$PACKAGE_DIR"
  done

echo "Uploading chart packages as Github releases"
# This will scan $PACKAGE_DIR for the tgz files that 'helm package' just generated, and upload them to the GitHub
# repo as Release artifacts. GitHub will create corresponding git tags for each chart.
docker run --user "$(id -u):$(id -g)" \
  -v "$(pwd)/$PACKAGE_DIR:/releases" \
  quay.io/helmpack/chart-releaser \
  cr upload \
  --package-path /releases \
  --owner atlassian-labs \
  --git-repo data-center-helm-charts \
  --token "$GITHUB_TOKEN"

echo "Regenerating chart repo index.yaml"
# This will fetch the index.yaml from the chart repo (NOT the local copy in this git repo), then fetch the list of
# release artifacts on GitHub, and add any missing releases to the index.yaml file. The updated file is then left in
# $PUBLISH_DIR for committing to git.
docker run \
  --user "$(id -u):$(id -g)" \
  -v "$(pwd)/$PUBLISH_DIR:/index" \
  -v "$(pwd)/$PACKAGE_DIR:/packages" \
  quay.io/helmpack/chart-releaser \
  cr index \
  --owner atlassian-labs \
  --git-repo data-center-helm-charts \
  --charts-repo https://atlassian-labs.github.io/data-center-helm-charts \
  --index-path /index/index.yaml \
  --package-path /packages \
  --token "$GITHUB_TOKEN"

git add $PUBLISH_DIR/index.yaml
