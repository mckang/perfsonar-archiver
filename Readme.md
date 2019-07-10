#Build
docker build -t mckang/pscheduler_graphite_archiver:v0.1 -f Dockerfile .

#Start
docker run --rm  -it --name archiver -p 8080:8080\
 -e GRAPHITE_SERVER_URL=http://192.168.99.1\
 -e CARBON_SERVER_ADDRESS=192.168.99.1\
 -e CARBON_SERVER_PORT=2003\
 -e PSCHEDULER_API_URL=https://192.168.99.10/pscheduler\
 -e LOGGING_LEVEL_COM_BEWISE_PSONAR=debug\
 mckang/pscheduler_graphite_archiver:v0.1
 
 #Graphite
 docker run --rm\
 --name graphite\
 -p 80:80\
 -p 2003-2004:2003-2004\
 -p 2023-2024:2023-2024\
 -p 8125:8125/udp\
 -p 8126:8126\
 graphiteapp/graphite-statsd
 
#Grafana
docker run --rm --name=grafana\
 -p 3000:3000 grafana/grafana
 
//only linux 
docker run --privileged -d -P --net=host -v "/var/run" perfsonar/testpoint


#/etc/pscheduler/default-archives/http.json

    {
      "data": {
        "_url": "http://192.168.99.1:8080/data/"
      },
      "archiver": "http"
    }

#/usr/share/pscheduler/api-server/pschedulerapiserver

import pscheduler

from .args import *

from flask import request

local_ips = pscheduler.LocalIPList()

def access_write_task(original_requester, key=None):
    """
    Determine whether a requester can write to a task or its runs.
    """

    return True

    requester = request.remote_addr

    # Local interfaces are always okay.
    if requester in local_ips:
        return True

    # Tasks without keys are limited to the original requester only
    if key is None:
        return requester == original_requester

    # Beyond here, the task has a key.  

    request_key = arg_string("key")

    return (request_key is not None) and (request_key == key)

    
    