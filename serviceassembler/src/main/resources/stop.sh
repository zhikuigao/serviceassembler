#stop service assembler server
ps -ef |grep com.jwis.serviceassembler.Application |awk '{print $2}'| xargs kill -9