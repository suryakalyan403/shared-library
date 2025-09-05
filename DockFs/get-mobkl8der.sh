#!/bin/sh

# Prepare the URL variable
file="mobkl8der.tar.gz"
url="MOBKL8DER/latest/$file"

echo "$file"
# Check for MDS_ID and MDS_SECRET
if [ -z "$MDS_ID" ] || [ -z "$MDS_SECRET" ]; then
    echo "Error: MDS_ID and MDS_SECRET must be set."
    exit 1
fi

# Output the URL
echo "Downloading $url..."

echo "curl \$(curl -s -L -X POST https://rl.raid.cloud/s3getobject -d \"id=$MDS_ID&secret=$MDS_SECRET)"

curl $(curl -s -L -X POST https://rl.raid.cloud/s3getobject -d "id=$MDS_ID&secret=$MDS_SECRET&objectname=$url")

docker login -u $MDS_ID -p $MDS_SECRET https://rl.raid.cloud/
