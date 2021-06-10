<?php
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);
$response = array();

$q = mysqli_query($con, "SELECT id_kendala,nama_kendala, FROM kendala ORDER BY kode_kendala");

$response["kendala"] = array();
while ($r = mysqli_fetch_array($q)) {
    $kendala = array();
    $kendala["id_kendala"] = $r['id_kendala'];
    $kendala["nama_kendala"] = $r['nama_kendala'];
    array_push($response["kendala"], $kendala);
}
$response["status"] = 0;
$response["message"] = "Get list kendala berhasil";

echo json_encode($response);
