<?php
$response = array();
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);

if (isset($input['id_kerusakan'])) {

    $id_kerusakan = $input['id_kerusakan'];
    $daftar_kendala = empty($input['daftar_kendala']) ? "" : $input['daftar_kendala'];

    mysqli_query($con, "delete from aturan where id_kerusakan='" . $id_kerusakan . "'");

    $hsl = explode("#", $daftar_kendala);
    foreach ($hsl as $val) {
        $q2 = mysqli_query($con, "select id_kendala from kendala where nama_kendala='$val'");
        if (mysqli_num_rows($q2) > 0) {
            $r2 = mysqli_fetch_array($q2);
            $id_kendala = $r2['id_kendala'];
            $q3 = "insert into aturan(id_kerusakan,id_kendala) values ('" . $id_kerusakan . "','" . $id_kendala . "')";
            mysqli_query($con, $q3);
        }
    }

    $response["status"] = 0;
    $response["message"] = "Data berhasil disimpan";
} else {
    $response["status"] = 2;
    $response["message"] = "Parameter ada yang kosong";
}
echo json_encode($response);
