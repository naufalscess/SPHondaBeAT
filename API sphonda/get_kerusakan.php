<?php
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);
$response = array();

if (isset($input['id_kerusakan'])) {

    $id_kerusakan = $input['id_kerusakan'];

    $q = mysqli_query($con, "SELECT kode_kerusakan,nama_kerusakan,deskripsi,solusi FROM kerusakan WHERE id_kerusakan='$id_kerusakan'");
    $r = mysqli_fetch_array($q);

    $response["status"] = 0;
    $response["message"] = "Get kerusakan berhasil";
    $response["kode_kerusakan"] = $r['kode_kerusakan'];
    $response["nama_kerusakan"] = $r['nama_kerusakan'];
    $response["deskripsi"] = $r['deskripsi'];
    $response["solusi"] = $r['solusi'];
} else {
    $response["status"] = 2;
    $response["message"] = "Parameter ada yang kosong";
}
echo json_encode($response);
