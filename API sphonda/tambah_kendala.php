<?php
$response = array();
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);

if (isset($input['kode_kendala'])) {

    $kode_kendala = $input['kode_kendala'];
    $nama_kendala = empty($input['nama_kendala']) ? "" : $input['nama_kendala'];

    $q = "INSERT INTO kendala(kode_kendala,nama_kendala,) VALUES ('$kode_kendala','$nama_kendala')";
    mysqli_query($con, $q);

    $response["status"] = 0;
    $response["message"] = "Data berhasil disimpan";
} else {
    $response["status"] = 2;
    $response["message"] = "Parameter ada yang kosong";
}
echo json_encode($response);
