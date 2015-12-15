from Crypto import Random
from Crypto.Cipher import AES
import base64


def main():
    string = "Seguranca 2015/2016"
    passphrase = "aaaaaaaaaaaaaaaa"

    print string
    e = encrypt(string, passphrase)
    print e
    d = decrypt(e, passphrase)
    print d

def encrypt(message, passphrase):
    IV = "bbbbbbbbbbbbbbbb"

    # passphrase MUST be 16, 24 or 32 bytes long, how can I do that ?
    aes = AES.new(passphrase, AES.MODE_CFB, IV)
    return aes.encrypt(message)

def decrypt(encrypted, passphrase):
    IV = "bbbbbbbbbbbbbbbb"

    aes = AES.new(passphrase, AES.MODE_CFB, IV)
    return aes.decrypt(encrypted)


if __name__ == '__main__':
    main()
