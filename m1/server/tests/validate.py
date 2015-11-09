#test the register_device endpoint

import requests

player_key="kR5OjIhyGFLT7hSk"
device_key="aaaaaaaaa"
so="Linux"
location = "PT"
book_id = "49941"
time = "10:10:10"

data = {'player_key': player_key,'device_key': device_key, 'so': so, 'location': location, 'book_id': book_id, 'time': time}
response = requests.post("http://127.0.0.1:8000/requests/validate/", data=data)

print response.text
