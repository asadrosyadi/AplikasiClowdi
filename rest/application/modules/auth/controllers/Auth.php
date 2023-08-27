<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Auth extends CI_Controller
{
    public function __construct()
    {
        parent::__construct();
        $this->load->library('form_validation');
    }
    
    public function rest() {
        $HWID = $this->input->post('HWID');
        $password = $this->input->post('password'); // Tanpa password_verify di sini
    
        $user = $this->db->where('HWID', $HWID)->get('user')->row();
        if ($user) {
            if (password_verify($password, $user->password)) {
                echo 'Sukses';
            } else {
                echo 'Gagal';
            }
        } else {
            echo 'Gagal';
        }
    }   

    function index(){
		$tolak = json_encode("access denied");
		echo $tolak;
	}
}
