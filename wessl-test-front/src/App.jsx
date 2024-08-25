import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
    // const [f_name, setFirstName] = useState('');
    // const [l_name, setLastName] = useState('');
    // const [phone, setPhone] = useState('');
    // const [msg, setMessage] = useState('');

    // const handleSubmit = async (e) => {
    //   e.preventDefault();

    //   const formData = {
    //     f_name,
    //     l_name,
    //     phone,
    //     msg,
    //   };

    //   try {
    //     const response = await fetch('http://localhost:3000/p/contact-us', {
    //     method: 'POST',
    //     headers: {
    //       'Content-Type': 'application/json',
    //       // 'X-XSRF-TOKEN': Cookies.get('csrftoken') || '',
    //      },
    //      body: JSON.stringify(formData),
    //        // credentials: 'include',
    //        withCredentials:true

    //    });

    //       if (!response.ok) {
    //         const errorData = await response.json();
    //         console.error('Response error:', errorData);
    //       } else {
    //         const data = await response.json();
    //         console.log('Success:', data);
    //       }
    //     } catch (error) {
    //       console.error('Error:', error);
    //     }
    //   };

    //   return (
    //       <div className="container my-5">
    //         <div className="row justify-content-center" style={{ backgroundColor: '#FAFAFA', borderRadius: '20px' }}>
    //           <div className="col-lg-8 mb-4 p-4">
    //             <form onSubmit={handleSubmit} className="col-lg-12 mt-5">
    //               <div className="row mb-3">
    //                 <div className="col">
    //                   <input
    //                     type="text"
    //                     className="form-control form-control-sm"
    //                     placeholder="الاسم الأول"
    //                     value={f_name}
    //                     onChange={(e) => setFirstName(e.target.value)}
    //                     required
    //                     style={{ height: '50px', borderRadius: '20px', fontSize: '1.1rem' }}
    //                   />
    //                 </div>
    //                 <div className="col">
    //                   <input
    //                     type="text"
    //                     className="form-control form-control-sm"
    //                     placeholder="الاسم الأخير"
    //                     value={l_name}
    //                     onChange={(e) => setLastName(e.target.value)}
    //                     required
    //                     style={{ height: '50px', borderRadius: '20px', fontSize: '1.1rem' }}
    //                   />
    //                 </div>
    //               </div>
    //               <div className="row mb-3">
    //                 <div className="col">
    //                   <input
    //                     type="text"
    //                     className="form-control form-control-sm"
    //                     placeholder="رقم الجوال"
    //                     value={phone}
    //                     onChange={(e) => setPhone(e.target.value)}
    //                     required
    //                     style={{ height: '50px', borderRadius: '20px', fontSize: '1.1rem' }}
    //                   />
    //                 </div>
    //               </div>
    //               <div className="row mb-3">
    //                 <div className="col">
    //                   <textarea
    //                     className="form-control form-control-sm p-3"
    //                     rows="5"
    //                     placeholder="الرسالة"
    //                     value={msg}
    //                     onChange={(e) => setMessage(e.target.value)}
    //                     required
    //                     style={{ height: '200px', borderRadius: '20px', fontSize: '1.1rem' }}
    //                   ></textarea>
    //                 </div>
    //               </div>
    //               <div className="text-center mt-5">
    //                 <button onClick={() => {handleSubmit();}}>ارسال</button>
    //               </div>
    //             </form>
    //           </div>
    //         </div>
    //       </div>
    //     );

  const [count, setCount] = useState(0)
  const sendPostRequest = async () => {
    const url = 'http://localhost:3000/p/contact-us';
    const requestData = {
      f_name: 'moataz',
      l_name: 'kayad',
      phone: '010985118194',
      msg: 'i cant belive my self'
    };

    try {
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData),
        withCredentials:true

      });

      if (response.ok) {
        const data = await response.json();
        console.log('Response:', data);
      } else {
        console.error('Error:', response.statusText);
      }
    } catch (error) {
      console.error('Request failed:', error);
    }
  };


  return (
    <>
      <div>
        <a href="https://vitejs.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Vite + React</h1>
      <div className="card">
        <button onClick={() => {
          sendPostRequest();
          setCount((count) => count + 1);
        }}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.jsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
    </>
  )
}

export default App
