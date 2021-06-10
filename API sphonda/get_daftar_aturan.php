<?php
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);
$response = array();

$q = mysqli_query($con, "SELECT id_kerusakan,nama_kerusakan FROM kerusakan ORDER BY id_kerusakan");

$response["aturan"] = array();
while ($r = mysqli_fetch_array($q)) {
    $id = $r['id_kerusakan'];
    $kendala = array();
    $qkendala = "select * from aturan where id_kendala='$id'";
    $qkendala = mysqli_query($con, $qkendala);
    while ($rkendala = mysqli_fetch_array($qkendala)) {
        $r_kendala = mysqli_fetch_array(mysqli_query($con, "select kode_kendala from kendala where id_kendala='" . $rkendala['id_kendala'] . "'"));
        $kendala[] = $r_kendala['kode_kendala'];
    }
    $daftar_kendala = implode(" - ", $kendala);
    $aturan = array();
    $aturan["id_kerusakan"] = $id;
    $aturan["nama_kerusakan"] = $r['nama_kerusakan'];
    $aturan["daftar_kendala"] = $daftar_kendala;
    array_push($response["aturan"], $aturan);
}
$response["status"] = 0;
$response["message"] = "Get list aturan berhasil";

echo json_encode($response);
