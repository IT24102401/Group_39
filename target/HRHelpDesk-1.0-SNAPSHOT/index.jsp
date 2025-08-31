<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HR Help Desk - Welcome</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Poppins', sans-serif;
        }

        body {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background: #0a0a0a;
            overflow: hidden;
        }

        .container {
            text-align: center;
            padding: 40px;
            max-width: 900px;
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.5);
            animation: slideIn 1.5s ease-in-out;
            position: relative;
            z-index: 2;
        }

        @keyframes slideIn {
            0% { opacity: 0; transform: translateY(-100px) scale(0.8); }
            100% { opacity: 1; transform: translateY(0) scale(1); }
        }

        h1 {
            font-size: 3.5em;
            color: #fff;
            text-transform: uppercase;
            letter-spacing: 3px;
            background: linear-gradient(45deg, #ff6b6b, #ffb700);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            animation: glow 2s infinite alternate;
        }

        @keyframes glow {
            0% { text-shadow: 0 0 10px rgba(255, 107, 107, 0.8); }
            100% { text-shadow: 0 0 20px rgb(255, 183, 0); }
        }

        p {
            font-size: 1.3em;
            color: #ddd;
            margin: 20px 0 40px;
            line-height: 1.7;
            animation: fadeInText 2s ease-in-out;
        }

        @keyframes fadeInText {
            0% { opacity: 0; }
            100% { opacity: 1; }
        }

        .login-btn {
            display: inline-block;
            padding: 15px 50px;
            background: linear-gradient(45deg, #ff6b6b, #ffb700);
            color: #fff;
            font-size: 1.2em;
            font-weight: 700;
            text-decoration: none;
            border-radius: 50px;
            transition: all 0.4s ease;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.4);
            position: relative;
            overflow: hidden;
        }

        .login-btn::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
            transition: 0.5s;
        }

        .login-btn:hover::before {
            left: 100%;
        }

        .login-btn:hover {
            transform: translateY(-5px) scale(1.05);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.6);
        }

        .features {
            display: flex;
            justify-content: space-around;
            flex-wrap: wrap;
            margin-top: 50px;
        }

        .feature-card {
            background: rgba(255, 255, 255, 0.05);
            border-radius: 15px;
            padding: 20px;
            width: 220px;
            margin: 15px;
            transition: transform 0.4s ease, box-shadow 0.4s ease;
            border: 1px solid rgba(255, 255, 255, 0.1);
        }

        .feature-card:hover {
            transform: translateY(-10px) rotate(2deg);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.5);
        }

        .feature-card h3 {
            font-size: 1.3em;
            color: #ffb700;
            margin-bottom: 10px;
        }

        .feature-card p {
            font-size: 0.95em;
            color: #bbb;
            margin: 0;
        }

        footer {
            margin-top: 50px;
            font-size: 0.9em;
            color: #888;
        }

        /* Particle Background */
        #particles-js {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
            z-index: 1;
        }

        @media (max-width: 600px) {
            h1 {
                font-size: 2.2em;
            }

            p {
                font-size: 1em;
            }

            .login-btn {
                padding: 12px 40px;
                font-size: 1em;
            }

            .feature-card {
                width: 100%;
                margin: 10px 0;
            }

            .container {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<div id="particles-js"></div>
<div class="container">
    <h1>HR Help Desk</h1>
    <p>Step into the future of HR support. Resolve queries, access resources, and empower your workforce with our cutting-edge platform.</p>

    <a href="jsp/login.jsp" class="login-btn">Login to HR Help Desk</a>

    <div class="features">
        <div class="feature-card">
            <h3>Instant Solutions</h3>
            <p>Resolve HR queries with lightning speed.</p>
        </div>
        <div class="feature-card">
            <h3>Global Access</h3>
            <p>Support available anytime, anywhere.</p>
        </div>
        <div class="feature-card">
            <h3>Fortified Security</h3>
            <p>Your data, locked down and safe.</p>
        </div>
    </div>

    <footer>
        &copy; <%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()) %> HR Help Desk. Powered by Batch02_Group_39_SKU.
    </footer>
</div>

<script src="https://cdn.jsdelivr.net/npm/particles.js@2.0.0/particles.min.js"></script>
<script>
    particlesJS("particles-js", {
        particles: {
            number: { value: 80, density: { enable: true, value_area: 800 } },
            color: { value: "#ffffff" },
            shape: { type: "circle", stroke: { width: 0, color: "#000000" } },
            opacity: { value: 0.5, random: true },
            size: { value: 3, random: true },
            line_linked: { enable: true, distance: 150, color: "#ffffff", opacity: 0.4, width: 1 },
            move: { enable: true, speed: 2, direction: "none", random: false, straight: false, out_mode: "out", bounce: false }
        },
        interactivity: {
            detect_on: "canvas",
            events: { onhover: { enable: true, mode: "repulse" }, onclick: { enable: true, mode: "push" }, resize: true },
            modes: { repulse: { distance: 100, duration: 0.4 }, push: { particles_nb: 4 } }
        },
        retina_detect: true
    });
</script>
</body>
</html>