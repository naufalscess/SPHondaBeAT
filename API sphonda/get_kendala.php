<?php
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);
$response = array();

if (isset($input['id_gejala'])) {

    $id_kendala = $input['id_kendala'];

    $q = mysqli_query($con, "SELECT kode_kendala,nama_kendala, FROM kendala WHERE id_kendala='$id_kendala'");
    $r = mysqli_fetch_array($q);

    $response["status"] = 0;
    $response["message"] = "Get kendala berhasil";
    $response["kode_kendala"] = $r['kode_kendala'];
    $response["nama_kendala"] = $r['nama_kendala'];
} else {
    $response["status"] = 2;
    $response["message"] = "Parameter ada yang kosong";
}
echo json_encode($response);
