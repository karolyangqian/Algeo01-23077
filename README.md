# Algeo01-23077
Referensi README: https://github.com/jehna/readme-best-practices/blob/master/README-default.md

# Cara Menjalankan Program
### Kompilasi
Keterangan: jalankan perintah di bawah pada workspace directory "ALGEO01-23077"
Output: bytecode yang disimpan di folder bin
```
javac -d bin src/algeo/*.java src/Main.java
```

Menjalankan bytecode (di direktori /bin)
```
java -classpath bin <NAMA_BYTECODE>
```
```
java -classpath bin Main

```

Menjalankan bytecode dalam sebuah module (e.g. algeo)
*Dilakukan untuk testing class implementation dengan driver main pada class itu sendiri.
```
java -classpath bin <NAMA_MODULE>/<NAMA_BYTECODE>
```
```
java -classpath bin algeo/<NAMA_BYTECODE>

```