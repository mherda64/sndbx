#!/bin/bash

time=$1
shift 1

container=$(docker run "$@")
code=$(timeout "$time" docker wait "$container" || true)
echo -n 'status: '
if [ -z "$code" ]; then
    echo timeout
else
    echo exited: $code
fi

echo output:
# pipe to sed simply for pretty nice indentation
docker logs $container

docker kill $container &> /dev/null

docker rm $container &> /dev/null
