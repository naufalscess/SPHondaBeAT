<?php
$response = array();
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);

if (isset($input['id_kerusakan'])) {

    $id_kerusakan = $input['id_kerusakan'];
    mysqli_query($con, "DELETE FROM kerusakan WHERE id_kerusakan='$id_kerusakan'");

    $response["status"] = 0;
    $response["message"] = "Data berhasil dihapus";
} else {
    $response["status"] = 2;
    $response["message"] = "Parameter ada yang kosong";
}
echo json_encode($response);
