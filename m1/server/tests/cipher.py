from Crypto.Cipher import AES
import os

def cipher():
    file_key="passwordpassword"
    with open("original.txt", 'rb') as book_file:
            with open("cipher.txt", 'wrb') as cipher_file:
                block_size = 64
                bytes_read = 0
                file_length = os.path.getsize("original.txt")

                while bytes_read < file_length:
                    block = book_file.read(block_size)
                    bytes_read += block_size
                    IV = "aaaaaaaaaaaaaaaa"
                    aes = AES.new(file_key, AES.MODE_CFB, IV)
                    ciphered_block = aes.encrypt(block)

                    cipher_file.write(ciphered_block)

                cipher_file.seek(0)


def decipher():
    file_key="passwordpassword"
    with open("cipher.txt", 'rb') as book_file:
            with open("after.txt", 'wrb') as cipher_file:
                block_size = 64
                bytes_read = 0
                file_length = os.path.getsize("cipher.txt")

                while bytes_read < file_length:
                    block = book_file.read(block_size)
                    bytes_read += block_size
                    IV = "aaaaaaaaaaaaaaaa"
                    aes = AES.new(file_key, AES.MODE_CFB, IV)
                    ciphered_block = aes.decrypt(block)

                    cipher_file.write(ciphered_block)

                cipher_file.seek(0)



if __name__ == '__main__':
    cipher()
    decipher()
