import React, { useState } from 'react';
import Cookies from 'js-cookie';
// import Header from '../components/header';
// import ButtonTwo from '../components/buttonTwo';

const Contact = () => {
  const [f_name, setFirstName] = useState('');
  const [l_name, setLastName] = useState('');
  const [phone, setPhone] = useState('');
  const [msg, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = {
      f_name,
      l_name,
      phone,
      msg,
    };

    try {
      const response = await fetch('http://localhost:3000/p/contact-us', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          // 'X-XSRF-TOKEN': Cookies.get('csrftoken') || '',
        },
        body: JSON.stringify(formData),
        // credentials: 'include',
        withCredentials:true

      });

      if (!response.ok) {
        const errorData = await response.json();
        console.error('Response error:', errorData);
      } else {
        const data = await response.json();
        console.log('Success:', data);
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };


  return (
    <div className="container my-5">
//       <Header content='ابق على اتصال معنا واسمعنا رأيك'/>
      <div className="row justify-content-center" style={{ backgroundColor: '#FAFAFA', borderRadius: '20px' }}>
        <div className="col-lg-8 mb-4 p-4">
          <form onSubmit={handleSubmit} className="col-lg-12 mt-5">
            <div className="row mb-3">
              <div className="col">
                <input
                  type="text"
                  className="form-control form-control-sm"
                  placeholder="الاسم الأول"
                  value={f_name}
                  onChange={(e) => setFirstName(e.target.value)}
                  required
                  style={{ height: '50px', borderRadius: '20px', fontSize: '1.1rem' }}
                />
              </div>
              <div className="col">
                <input
                  type="text"
                  className="form-control form-control-sm"
                  placeholder="الاسم الأخير"
                  value={l_name}
                  onChange={(e) => setLastName(e.target.value)}
                  required
                  style={{ height: '50px', borderRadius: '20px', fontSize: '1.1rem' }}
                />
              </div>
            </div>
            <div className="row mb-3">
              <div className="col">
                <input
                  type="text"
                  className="form-control form-control-sm"
                  placeholder="رقم الجوال"
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  required
                  style={{ height: '50px', borderRadius: '20px', fontSize: '1.1rem' }}
                />
              </div>
            </div>
            <div className="row mb-3">
              <div className="col">
                <textarea
                  className="form-control form-control-sm p-3"
                  rows="5"
                  placeholder="الرسالة"
                  value={msg}
                  onChange={(e) => setMessage(e.target.value)}
                  required
                  style={{ height: '200px', borderRadius: '20px', fontSize: '1.1rem' }}
                ></textarea>
              </div>
            </div>
            <div className="text-center mt-5">
              <button onClick={() => {sendPostRequest();}}>ارسال</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Contact;