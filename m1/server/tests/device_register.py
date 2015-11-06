#test the register_device endpoint

import requests

user="bms@ua.pt"
device_key="aaaaaaaaaaaaaaaa"
device_name="Bruno's Computer"

data = {'user': user,'device_key': device_key, 'device_name': device_name}
response = requests.post("http://127.0.0.1:8000/users/register_device/", data=data)

print response.text
