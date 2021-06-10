<?php
$server = isset($_SERVER['PHP_ENV']) ? $_SERVER['PHP_ENV'] : 'development';

if ($server == 'development') {
    $db_host = 'localhost';
    $db_user = 'root';
    $db_password = '';
    $db_name = 'sp_honda';
} elseif ($server == 'production') {
    $db_host = 'localhost';
    $db_user = 'root';
    $db_password = '';
    $db_name = 'sp_honda';
}

$con = @mysqli_connect($db_host, $db_user, $db_password) or die('<center><strong>Gagal koneksi ke server database</strong></center>');
mysqli_select_db($con, $db_name) or die('<center><strong>Database tidak ditemukan</strong></center>');
