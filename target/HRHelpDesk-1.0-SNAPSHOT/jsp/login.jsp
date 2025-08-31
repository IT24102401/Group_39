<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>HR Help Desk Login</title>
    <link rel="stylesheet" href="../css/styles.css">
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
            padding: 30px;
            max-width: 400px;
            background: rgba(255, 255, 255, 0.15);
            backdrop-filter: blur(12px);
            border-radius: 15px;
            border: 2px solid rgba(255, 255, 255, 0.3);
            box-shadow: 0 0 20px rgba(123, 44, 191, 0.5);
            animation: scaleIn 1.2s ease-in-out;
            position: relative;
            z-index: 2;
        }

        @keyframes scaleIn {
            0% { opacity: 0; transform: scale(0.7); }
            100% { opacity: 1; transform: scale(1); }
        }

        h2 {
            font-size: 2.2em;
            color: #fff;
            text-transform: uppercase;
            letter-spacing: 2px;
            background: linear-gradient(45deg, #00c4b4, #7b2cbf);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            animation: pulseGlow 1.5s infinite alternate;
            margin-bottom: 25px;
        }

        @keyframes pulseGlow {
            0% { text-shadow: 0 0 8px rgba(0, 196, 180, 0.7); }
            100% { text-shadow: 0 0 15px rgba(123, 44, 191, 0.7); }
        }

        .error {
            color: #ff4d6d;
            font-size: 1em;
            margin-bottom: 15px;
            animation: fadeInText 1s ease-in-out;
        }

        @keyframes fadeInText {
            0% { opacity: 0; }
            100% { opacity: 1; }
        }

        form {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 12px;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            max-width: 280px;
            padding: 10px;
            border: none;
            border-radius: 20px;
            background: rgba(255, 255, 255, 0.25);
            color: #fff;
            font-size: 0.95em;
            outline: none;
            transition: all 0.3s ease;
        }

        input[type="text"]::placeholder, input[type="password"]::placeholder {
            color: rgba(255, 255, 255, 0.7);
        }

        input[type="text"]:focus, input[type="password"]:focus {
            background: rgba(255, 255, 255, 0.35);
            box-shadow: 0 0 12px rgba(123, 44, 191, 0.6);
        }

        input[type="submit"] {
            padding: 10px 35px;
            background: linear-gradient(45deg, #00c4b4, #7b2cbf);
            color: #fff;
            font-size: 1em;
            font-weight: 600;
            border: none;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 5px 12px rgba(0, 0, 0, 0.4);
            animation: pulseButton 2s infinite;
        }

        @keyframes pulseButton {
            0% { transform: scale(1); }
            50% { transform: scale(1.05); }
            100% { transform: scale(1); }
        }

        input[type="submit"]:hover {
            box-shadow: 0 8px 20px rgba(123, 44, 191, 0.6);
        }

        #particles-js {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
            z-index: 1;
        }

        @media (max-width: 600px) {
            h2 {
                font-size: 1.6em;
            }

            .container {
                padding: 15px;
                max-width: 85%;
            }

            input[type="text"], input[type="password"] {
                max-width: 240px;
            }

            input[type="submit"] {
                padding: 8px 30px;
                font-size: 0.9em;
            }
        }
    </style>
</head>
<body>
<div id="particles-js"></div>
<div class="container">
    <h2>HR Help Desk Login</h2>
    <% if (request.getAttribute("error") != null) { %>
    <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>

    <form action="${pageContext.request.contextPath}/loginServlet" method="post">
        <input type="text" name="username" placeholder="Username" required>
        <input type="password" name="password" placeholder="Password" required>
        <input type="submit" value="Login">
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/particles.js@2.0.0/particles.min.js"></script>
<script>
    particlesJS("particles-js", {
        particles: {
            number: { value: 60, density: { enable: true, value_area: 800 } },
            color: { value: "#7b2cbf" },
            shape: { type: "circle", stroke: { width: 0, color: "#000000" } },
            opacity: { value: 0.6, random: true },
            size: { value: 4, random: true },
            line_linked: { enable: true, distance: 120, color: "#00c4b4", opacity: 0.5, width: 1.5 },
            move: { enable: true, speed: 3, direction: "none", random: true, straight: false, out_mode: "out", bounce: false }
        },
        interactivity: {
            detect_on: "canvas",
            events: { onhover: { enable: true, mode: "grab" }, onclick: { enable: true, mode: "push" }, resize: true },
            modes: { grab: { distance: 140, line_linked: { opacity: 0.7 } }, push: { particles_nb: 3 } }
        },
        retina_detect: true
    });
</script>
</body>
</html>