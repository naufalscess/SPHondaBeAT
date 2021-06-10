<?php
$response = array();
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);

if (isset($input['kode_kerusakan'])) {

    $kode_kerusakan = $input['kode_kerusakan'];
    $nama_kerusakan = empty($input['nama_kerusakan']) ? "" : $input['nama_kerusakan'];
    $deskripsi = empty($input['deskripsi']) ? "" : $input['deskripsi'];
    $solusi = empty($input['solusi']) ? "" : $input['solusi'];

    $q = "INSERT INTO kerusakan(kode_kerusakan,nama_kerusakan,deskripsi,solusi) VALUES ('$kode_kerusakan','$nama_kerusakan','$deskripsi','$solusi')";
    mysqli_query($con, $q);

    $response["status"] = 0;
    $response["message"] = "Data berhasil disimpan";
} else {
    $response["status"] = 2;
    $response["message"] = "Parameter ada yang kosong";
}
echo json_encode($response);
