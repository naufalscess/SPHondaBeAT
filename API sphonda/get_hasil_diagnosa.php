<?php
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);
$response = array();

if (isset($input['hasil'])) {

    $hasil = $input['hasil'];
    $id_pengguna = $input['id_pengguna'];
    $metode = $input['metode'];
    $tanggal = date('Y-m-d');

    $arr_kendala_terpilih = explode("#", $hasil);

    $id_kerusakan_hasil = '';
    $nama_kerusakan_hasil = '';
    $sql1 = mysqli_query($con, "select id_kerusakan,nama_kerusakan from kerusakan order by id_kerusakan");
    while ($r = mysqli_fetch_array($sql1)) {
        $id_kerusakan = $r['id_kerusakan'];
        $nama_kerusakan = $r['nama_kerusakan'];
        $arr_kendala_kerusakan = array();
        $sql_at = mysqli_query($con, "select id_kendala from aturan where id_kerusakan='$id_kerusakan' order by id_kendala");
        while ($r_at = mysqli_fetch_array($sql_at)) {
            $id_kendala = $r_at['id_kendala'];
            $arr_kendala_kerusakan[] = $id_kendala;
        }
        if (arrays_are_equal($arr_kendala_terpilih, $arr_kendala_kerusakan)) {
            $id_kerusakan_hasil = $id_kerusakan;
            $nama_kerusakan_hasil = $nama_kerusakan;
        }
    }

    if ($nama_kerusakan_hasil != '') {
        $q = "insert into riwayat(id_pengguna,id_kerusakan,tanggal,metode) values ('" . $id_pengguna . "','" . $id_kerusakan_hasil . "','" . $tanggal . "','" . $metode . "')";
        mysqli_query($con, $q);
    } else {
        $nama_kerusakan_hasil = 'Tidak ada jenis penyakit yang sesuai dengan kendala terpilih';
        $q = "insert into riwayat(id_pengguna,tanggal,metode) values ('" . $id_pengguna . "','" . $tanggal . "','" . $metode . "')";
        mysqli_query($con, $q);
    }

    $response["status"] = 0;
    $response["id_kerusakan"] = $id_kerusakan_hasil;
    $response["nama_kerusakan"] = $nama_kerusakan_hasil;
} else {
    $response["status"] = 2;
    $response["message"] = "Parameter ada yang kosong";
}

function arrays_are_equal($array1, $array2)
{
    array_multisort($array1);
    array_multisort($array2);
    return (serialize($array1) === serialize($array2));
}

echo json_encode($response);
