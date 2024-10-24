

# Algeo01-23077
Albertus Christian Poandy | 13523077 </br>
Grace Evelyn Simon | 13523087 </br>
Karol Yangqian Poetracahaya | 13523093

# Deskripsi Program
Program ini merupakan aplikasi kalkulator matriks yang memiliki 7 menu utama, yakni perhitungan Sistem Persamaan Linier, Determinan, Matriks Balikan, Interpolasi Polinomial, Regresi Berganda, *Bicubic Spline Interpolation*, dan *Image Resizer*.
# Cara Clone Repo

```
git clone https://github.com/karolyangqian/Algeo01-23077.git
```
# Cara Menjalankan Program
### Kompilasi
Keterangan: jalankan perintah di bawah pada workspace directory "Algeo01-23077"
Output: bytecode yang disimpan di folder bin
```
javac -d bin src/gui/src/main/java/algeo/*.java src/gui/src/main/java/algeo/Main.java
```

Menjalankan bytecode dalam sebuah module (e.g. algeo) </br>
*Dilakukan untuk testing class implementation dengan driver main pada class itu sendiri.
```
java -classpath bin <NAMA_MODULE>/<NAMA_BYTECODE>
```
```
java -classpath bin algeo/<NAMA_BYTECODE>
```

# Cara Menjalankan GUI
1. Pastikan Java Development Kit (JDK) telah terinstall.
2. Lakukan *clone* repository atau klik link berikut untuk melakukan proses [download](https://github.com/karolyangqian/Algeo01-23077/raw/refs/heads/main/Algeo01-23077.jar) file executable `Algeo01-23077.jar`.
3. _Double click_ file executable `Algeo01-23077.jar` untuk menjalankan GUI program.


# Alternatif Cara Menjalankan GUI
GUI proyek ini di-*build* menggunakan Maven (sebuah *build tool* untuk JavaFX). Berikut adalah langkah-langkah untuk melakukan *setup* Maven.
1. Pastikan Java Development Kit (JDK) sudah terinstall.
2. Download zip binary Maven (version 3.9.9 saat proyek ini dibuat) untuk windows dari tautan https://maven.apache.org/download.cgi. 
3. Lakukan ekstraksi file .zip tersebut dan simpan pada *directory* yang mudah diakses seperti `C:\Program Files\Apache\Maven`. Seharusnya Anda memperoleh hasil seperti `C:\Program Files\Apache\Maven\apache-maven-3.x.x`.
4. Buat system variable baru dengan *name* `MAVEN_HOME` dan value `C:\Program Files\Apache\Maven`.
5. Masukkan path `bin` pada direktori `apache-maven-3.x.x` ke path environment variables. 

Setelah melakukan setup Maven, ada 2 cara untuk menjalankan GUI
### javafx:run
1. Buka Maven explorer.
2. Tekan gui -> Plugins -> javafx -> javafx:run
### javafx:jlink
1. Buka Maven explorer.
2. Tekan gui -> Plugins -> javafx -> javafx:jlink
3. Jalankan `.\src\gui\target\image\bin\launcher` pada terminal.

</br></br>
![alt text](icon.jpg)