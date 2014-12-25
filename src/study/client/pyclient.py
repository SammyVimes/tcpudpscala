# coding=utf-8

import socket
import json

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

server_address = ('localhost', 5555)

print "Enter country code: "
country_code = raw_input()

sock.sendto(country_code, server_address)
data, server = sock.recvfrom(256)
print data

