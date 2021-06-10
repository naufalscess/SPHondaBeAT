<?php
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);
$response = array();

$q = mysqli_query($con, "SELECT id_kerusakan,nama_kerusakan FROM kerusakan ORDER BY id_kerusakan");

$response['kerusakan'] = array();
while ($r = mysqli_fetch_array($q)) {

    $kerusakan = array();
    $kerusakan['id_kerusakan'] = $r['id_kerusakan'];
    $kerusakan['nama_kerusakan'] = $r['nama_kerusakan'];

    $kendala = array();
    $id = $r['id_kerusakan'];
    $qkendala = "select * from aturan where id_kerusakan='$id'";
    $qkendala = mysqli_query($con, $qgejala);
    while ($rkendala = mysqli_fetch_array($qkendala)) {
        $r_kendala = mysqli_fetch_array(mysqli_query($con, "select id_kendala,nama_kendala, from kendala where id_kendala='" . $rkendala['id_kendala'] . "'"));
        $kendala[] = array(
            'id_kendala' => $r_kendala['id_kendala'],
            'nama_kendala' => $r_kendala['nama_kendala'],
            );
    }
    $kerusakan['kendala'] = $kendala;

    array_push($response['kerusakan'], $kerusakan);
}

$response['status'] = 0;
$response['message'] = "Get list kendala berhasil";

echo json_encode($response);
