<?php
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);
$response = array();

$q = mysqli_query($con, "SELECT id_kerusakan,nama_kerusakan FROM kerusakan ORDER BY id_kerusakan");

$response["kerusakan"] = array();
while ($r = mysqli_fetch_array($q)) {
    $penyakit = array();
    $penyakit["id_kerusakan"] = $r['id_kerusakan'];
    $penyakit["nama_kerusakan"] = $r['nama_kerusakan'];
    array_push($response["kerusakan"], $kerusakan);
}
$response["status"] = 0;
$response["message"] = "Get list kerusakan berhasil";

echo json_encode($response);
