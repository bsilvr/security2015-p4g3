from Crypto.Cipher import AES
import os

def cipher():
    file_key="passwordpassword"
    with open("original.txt", 'rb+') as book_file:
            with open("cipher.txt", 'wrb') as cipher_file:
                block_size = 16
                bytes_read = 0

                data = book_file.read()
                length = 16 - (len(data) % 16)
                data += chr(length)*length
                book_file.seek(0)
                IV = "aaaaaaaaaaaaaaaa"
                aes = AES.new(file_key, AES.MODE_CBC, IV)
                size = len(data)
                while bytes_read < size:
                    block = data[0:block_size]
                    data = data[block_size:]
                    bytes_read += block_size
                    ciphered_block = aes.encrypt(block)
                    cipher_file.write(ciphered_block)

                cipher_file.seek(0)
                cipher_file.close()


def decipher():
    file_key="passwordpassword"
    with open("cipher.txt", 'rb+') as book_file:
            with open("after.txt", 'wrb') as cipher_file:
                block_size = 16
                bytes_read = 0
                file_length = os.path.getsize("cipher.txt")
                data = book_file.read()
                print "file_length: " + str(len(data))
                book_file.seek(0)
                IV = "aaaaaaaaaaaaaaaa"
                aes = AES.new(file_key, AES.MODE_CBC, IV)
                while bytes_read < file_length:
                    block = book_file.read(block_size)
                    print "block: " + block

                    bytes_read += block_size
                    ciphered_block = aes.decrypt(block)
                    print "deciphered: " + ciphered_block
                    if file_length - bytes_read < 16:
                        print ciphered_block[-1]
                        tmp = ciphered_block[-1]
                        ciphered_block = ciphered_block[:-7]
                    cipher_file.write(ciphered_block)

                cipher_file.seek(0)



if __name__ == '__main__':
    cipher()
    #decipher()
