<?php
class Rest extends MX_Controller{
function __construct() {
parent::__construct();
//is_logged_in();
	
}
	function index(){
		$tolak = json_encode("access denied");
		echo $tolak;
	}


	function user(){
			
		$user = $this->db->select('*')->from('user')->where('HWID', $this->uri->segment(3))->limit(1)->order_by('id', 'DESC')->get()->result(); //Untuk mengambil data dari database webinar
		$baca_sensor = $this->db->select('*')->from('datasensor')->where('HWID', $this->uri->segment(3))->limit(1)->order_by('id', 'DESC')->get()->result();	
			$response = array("user" => array());
				foreach ($user as $r) foreach ($baca_sensor as $b) {
				$temp = array(
				"nama" => $r->nama,
				"email" => $r->email,
				"token" => $r->token,
				"HWID" => $r->HWID,
				"kondisi_mesin" => $r->kondisi_mesin,
				"mode_mesin" => $r->mode_mesin,
				"heater" => $r->heater,
				"kondisi_jemuran" => $r->kondisi_jemuran,
				"nilai_kering" => $r->nilai_kering,
				"batas_suhu" => $r->batas_suhu,
				"batas_nilai_hujan" => $r->batas_nilai_hujan,
				"batas_nilai_siang" => $r->batas_nilai_siang,
				"password" => $r->password,
				"time" => $b->time,
				"rh" => $b->rh,
				"suhu_luar" => $b->suhu_luar,
				"suhu_dalam" => $b->suhu_dalam,
				"rain_drop" => $b->rain_drop,
				"ldr" => $b->ldr
				
			);
				
				array_push($response["user"], $temp);
			}
			$data = json_encode($response);
			echo "$data";
    }


	public function buatuser()
{
    $nama = isset($_GET['nama']) ? $_GET['nama'] : '';
    $email = isset($_GET['email']) ? $_GET['email'] : '';
    $HWID = isset($_GET['HWID']) ? $_GET['HWID'] : '';
    $password = isset($_GET['password']) ? $_GET['password'] : '';

    // Periksa apakah email dan HWID sudah ada dalam database
    $this->db->where('email', $email);
    $this->db->or_where('HWID', $HWID);
    $existingUser = $this->db->get('user')->row();

    if ($existingUser) {
        echo "HWID atau Email Sudah Terdaftar"; // Pengiriman gagal jika email atau HWID sudah ada
    } else {
        $randomBytes = random_bytes(16); // Menghasilkan 16 byte acak
        $token = bin2hex($randomBytes); // Konversi ke format heksadesimal

        $isi = array(
            'nama'             => $nama,
            'email'            => $email,
            'HWID'             => $HWID,
            'password'         => password_hash($password, PASSWORD_DEFAULT),
            'token'            => $token,
            'kondisi_mesin'    => 'OFF',
            'mode_mesin'       => 'Otomatis',
            'heater'           => 'OFF',
            'kondisi_jemuran'  => 'Dalam',
            'nilai_kering'     => 20,
            'batas_suhu'       => 23,
            'batas_nilai_hujan'=> 100,
            'batas_nilai_siang'=> 115
        );

        $this->db->insert('user', $isi);

        $isi2 = array(
            'email'            => $email,
            'HWID'             => $HWID,
			'rh'     => 0,
			'suhu_luar'     => 0,
			'suhu_dalam'     => 0,
			'rain_drop'     => 0,
			'ldr'     => 0,
			'kondisi_jemuran'     => "dalam",
			'kondisi_heater'     => "OFF",
			'kondisi_mesin'     => "OFF"
        );
		$this->db->insert('datasensor', $isi2);
        $this->db->trans_complete();
        if ($this->db->trans_status() === FALSE)
        {
            echo "gagal";
        } else {
            echo "sukses";
        }
    }
}
	
	function updateuser(){
	if($_SERVER['REQUEST_METHOD']=='POST'){
        $data = array(
            'nilai_kering'     => $this->input->post('nilai_kering'),
            'batas_suhu'     => $this->input->post('batas_suhu'),
            'batas_nilai_hujan'     => $this->input->post('batas_nilai_hujan'),
            'batas_nilai_siang'     => $this->input->post('batas_nilai_siang'),
            'nama'     => $this->input->post('nama'),
			'email'     => $this->input->post('email'),
			'token'     => $this->input->post('token'),
			'password' => password_hash($this->input->post('password'), PASSWORD_DEFAULT)

			
        );
        $hwid   = $this->input->post('HWID');
        $this->db->where('HWID',$hwid);
        $this->db->update('user',$data);
        
		$this->db->trans_complete();
		if ($this->db->trans_status() === FALSE)
		{
			echo "gagal";
		} else {
			echo "sukses";
		}
        }
	
    }


	function updateswitchmesin(){
		if($_SERVER['REQUEST_METHOD']=='POST'){
			$data = array(
				'kondisi_mesin'     => $this->input->post('kondisi_mesin')
				
			);
			$hwid   = $this->input->post('HWID');
			$this->db->where('HWID',$hwid);
			$this->db->update('user',$data);
			
			$this->db->trans_complete();
			if ($this->db->trans_status() === FALSE)
			{
				echo "gagal";
			} else {
				echo "sukses";
			}
			}
		
		}

		function updateswitchmesin2(){
			if($_SERVER['REQUEST_METHOD']=='POST'){
				$data = array(
					'mode_mesin'     => $this->input->post('mode_mesin')
					
				);
				$hwid   = $this->input->post('HWID');
				$this->db->where('HWID',$hwid);
				$this->db->update('user',$data);
				
				$this->db->trans_complete();
				if ($this->db->trans_status() === FALSE)
				{
					echo "gagal";
				} else {
					echo "sukses";
				}
				}
			
			}

			function updateswitchlengan(){
				if($_SERVER['REQUEST_METHOD']=='POST'){
					$data = array(
						'kondisi_jemuran'     => $this->input->post('kondisi_jemuran')

					);
					$hwid   = $this->input->post('HWID');
					$this->db->where('HWID',$hwid);
					$this->db->update('user',$data);
					
					$this->db->trans_complete();
					if ($this->db->trans_status() === FALSE)
					{
						echo "gagal";
					} else {
						echo "sukses";
					}
					}
				
				}		
				
				
				function updateswitchheater(){
					if($_SERVER['REQUEST_METHOD']=='POST'){
						$data = array(
							'heater'     => $this->input->post('heater')
													
						);
						$hwid   = $this->input->post('HWID');
						$this->db->where('HWID',$hwid);
						$this->db->update('user',$data);
						
						$this->db->trans_complete();
						if ($this->db->trans_status() === FALSE)
						{
							echo "gagal";
						} else {
							echo "sukses";
						}
						}
					
					}			


		function updateoutput(){
			if($_SERVER['REQUEST_METHOD']=='POST'){
				$data = array(
					'kondisi_mesin'     => $this->input->post('kondisi_mesin'),
					'mode_mesin'     => $this->input->post('mode_mesin'),
					'heater'     => $this->input->post('heater'),
					'kondisi_jemuran'     => $this->input->post('kondisi_jemuran')
					
				);
				$hwid   = $this->input->post('HWID');
				$this->db->where('HWID',$hwid);
				$this->db->update('user',$data);
				
				$this->db->trans_complete();
				if ($this->db->trans_status() === FALSE)
				{
					echo "gagal";
				} else {
					echo "sukses";
				}
				}
			
			}

	
	public function kirimdatasensor()
	{
	  $this->db->select('*')->from('user')->where('HWID', $_GET['HWID'])->where('token', $_GET['token'])->get()->row()->id;
	  $isi = array(
  
		'HWID'     => $_GET['HWID'],
		'email'     => $_GET['email'],
		'rh'     => $_GET['rh'],
		'suhu_luar'     => $_GET['suhu_luar'],
		'suhu_dalam'     => $_GET['suhu_dalam'],
		'rain_drop'     => $_GET['rain_drop'],
		'ldr'     => $_GET['ldr'],
		'kondisi_jemuran'     => $_GET['kondisi_jemuran'],
		'kondisi_heater'     => $_GET['kondisi_heater'],
		'kondisi_mesin'     => $_GET['kondisi_mesin']
		
  
	  );
	  $this->db->insert('datasensor', $isi);
	  $this->db->trans_complete();
	  if ($this->db->trans_status() === FALSE)
	  {
		  echo "gagal";
	  } else {
		  echo "sukses";
	  }
	}

	function log_sensor(){
			
		$log_sensor = $this->db->select('*')->from('datasensor')->where('HWID', $this->uri->segment(3))->order_by('id', 'DESC')->get()->result(); //Untuk mengambil data dari database webinar
			$response = array("log_sensor" => array());
				foreach ($log_sensor as $r) {
				$temp = array(
				"time" => $r->time,
				"rh" => $r->rh,
				"suhu_luar" => $r->suhu_luar,
				"suhu_dalam" => $r->suhu_dalam,
				"rain_drop" => $r->rain_drop,
				"ldr" => $r->ldr,
				"kondisi_jemuran" => $r->kondisi_jemuran,
				"kondisi_heater" => $r->kondisi_heater,
				"kondisi_mesin" => $r->kondisi_mesin
			);
				
				array_push($response["log_sensor"], $temp);
			}
			$data = json_encode($response);
			echo "$data";
    }


	

	
}