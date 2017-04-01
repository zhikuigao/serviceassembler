#!/usr/bin/bash
#netstat -tnlp|grep -w 10081|awk '{print $7}'|awk -F'/' '{print $1}'
netstat -tnl | grep -w   10040
exit 0