<?php
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);
$response = array();

if (isset($input['id_kerusakan'])) {

    $id_kerusakan = $input['id_kerusakan'];

    $q = mysqli_query($con, "SELECT id_kerusakan,nama_kerusakan FROM kerusakan WHERE id_kerusakan='$id_kerusakan'");
    $r = mysqli_fetch_array($q);

    $kendala = array();
    $qkendala = "select * from aturan where id_penyakit='$id_penyakit'";
    $qkendala = mysqli_query($con, $qkendala);
    while ($rkendala = mysqli_fetch_array($qkendala)) {
        $r_kendala = mysqli_fetch_array(mysqli_query($con, "select kode_kendala,nama_kendala from kendala where id_kendala='" . $rkendala['id_kendala'] . "'"));
        $kendala[] = $r_kendala['kode_kendala'] . ' - ' . $r_kendala['nama_kendala'];
    }
    $daftar_kendala = implode("\n", $kendala);

    $response["status"] = 0;
    $response["message"] = "Get aturan berhasil";
    $response["id_kerusakan"] = $r['id_kerusakan'];
    $response["nama_kerusakan"] = $r['nama_kerusakan'];
    $response["daftar_kendala"] = $daftar_kendala;
} else {
    $response["status"] = 2;
    $response["message"] = "Parameter ada yang kosong";
}
echo json_encode($response);
