#test the register_device endpoint

import requests


book_id="49941"

data = {'book_id': book_id}
response = requests.post("http://127.0.0.1:8000/requests/get_file/", data=data)

print response.text
