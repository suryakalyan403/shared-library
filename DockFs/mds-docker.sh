curl -sLX POST "https://$REG_URL/auth?f=skopeo" -d "id=$MDS_ID&secret=$MDS_SECRET" | sh

skopeo inspect docker://$SKP_REG_URL/aip/$IMG_NAME:$TAG
skopeo copy docker://$SKP_REG_URL/aip/$IMG_NAME:$TAG docker-archive:$IMG_NAME_$TAG.tar
