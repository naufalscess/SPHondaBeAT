<?php
$response = array();
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);

if (isset($input['id_kerusakan'])) {

    $id_kerusakan = $input['id_kerusakan'];
    $kode_kerusakan = empty($input['kode_kerusakan']) ? "" : $input['kode_kerusakan'];
    $nama_kerusakan = empty($input['nama_kerusakan']) ? "" : $input['nama_kerusakan'];
    $deskripsi = empty($input['deskripsi']) ? "" : $input['deskripsi'];
    $solusi = empty($input['solusi']) ? "" : $input['solusi'];

    $q = "UPDATE kerusakan SET
			kode_kerusakan='$kode_kerusakan',
			nama_kerusakan='$nama_kerusakan',
			deskripsi='$deskripsi',
			solusi='$solusi'
		WHERE id_kerusakan='$id_kerusakan'";
    mysqli_query($con, $q);

    $response["status"] = 0;
    $response["message"] = "Data berhasil disimpan";
} else {
    $response["status"] = 2;
    $response["message"] = "Parameter ada yang kosong";
}
echo json_encode($response);
