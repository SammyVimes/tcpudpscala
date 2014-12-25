# coding=utf-8

import socket

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(('localhost', 5555))

server_address = ('localhost', 5555)

print "Enter message (q to finish): "

country_code = None
while country_code != "q":
    country_code = raw_input()
    sock.sendall(country_code + "\n")
data = sock.recv(256)
print data

