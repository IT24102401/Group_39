<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>HR Help Desk Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Inter', sans-serif;
        }

        body {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            animation: gradientShift 8s ease-in-out infinite;
            overflow: hidden;
            position: relative;
        }

        #particles-js {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
            z-index: 1;
        }

        @keyframes gradientShift {
            0%, 100% { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
            50% { background: linear-gradient(135deg, #764ba2 0%, #667eea 100%); }
        }

        .container {
            text-align: center;
            padding: 40px;
            max-width: 450px;
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 16px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            box-shadow: 0 0 30px rgba(0, 0, 0, 0.3);
            position: relative;
            z-index: 2;
            animation: fadeIn 1s ease-out;
        }

        @keyframes fadeIn {
            0% { opacity: 0; transform: translateY(20px); }
            100% { opacity: 1; transform: translateY(0); }
        }

        h2 {
            font-size: 2.5em;
            color: #fff;
            text-transform: uppercase;
            letter-spacing: 3px;
            margin-bottom: 25px;
            background: rgba(255, 255, 255, 0.15);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.2);
            border-radius: 30px;
            padding: 10px 20px;
            animation: pulseGlow 2s ease-in-out infinite;
        }

        @keyframes pulseGlow {
            0%, 100% { box-shadow: 0 0 20px rgba(255, 255, 255, 0.3); }
            50% { box-shadow: 0 0 40px rgba(255, 255, 255, 0.6); }
        }

        .error {
            color: rgba(255, 255, 255, 0.8);
            font-size: 0.9em;
            margin-bottom: 15px;
            animation: shake 0.3s ease-in-out;
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-5px); }
            75% { transform: translateX(5px); }
        }

        form {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 18px;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            max-width: 320px;
            padding: 14px;
            border: none;
            border-radius: 30px;
            background: rgba(255, 255, 255, 0.15);
            color: #fff;
            font-size: 1em;
            outline: none;
            transition: all 0.3s ease;
            box-shadow: 0 0 8px rgba(255, 255, 255, 0.2);
        }

        input[type="text"]::placeholder, input[type="password"]::placeholder {
            color: rgba(255, 255, 255, 0.6);
        }

        input[type="text"]:focus, input[type="password"]:focus {
            background: rgba(255, 255, 255, 0.25);
            box-shadow: 0 0 12px rgba(255, 255, 255, 0.5);
            transform: scale(1.02);
        }

        input[type="submit"] {
            padding: 14px 50px;
            background: linear-gradient(90deg, #667eea, #764ba2);
            color: #fff;
            font-size: 1.1em;
            font-weight: 600;
            border: none;
            border-radius: 30px;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(255, 255, 255, 0.4);
            position: relative;
            overflow: hidden;
        }

        input[type="submit"]::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
            transition: 0.5s;
        }

        input[type="submit"]:hover::before {
            left: 100%;
        }

        input[type="submit"]:hover {
            background: linear-gradient(90deg, #764ba2, #667eea);
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(255, 255, 255, 0.6);
        }

        @media (max-width: 600px) {
            h2 {
                font-size: 1.8em;
            }

            .container {
                padding: 20px;
                max-width: 90%;
            }

            input[type="text"], input[type="password"] {
                max-width: 280px;
            }

            input[type="submit"] {
                padding: 12px 40px;
                font-size: 1em;
            }
        }
    </style>
</head>
<body>
<div id="particles-js"></div>
<div class="container">
    <h2>Help Desk Login</h2>
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
    // Check if particlesJS is defined before initializing
    if (typeof particlesJS !== 'undefined') {
        particlesJS("particles-js", {
            particles: {
                number: { value: 80, density: { enable: true, value_area: 800 } },
                color: { value: ["#667eea", "#764ba2", "#ffffff"] },
                shape: { type: ["circle", "triangle"], stroke: { width: 0 } },
                opacity: { value: 0.6, random: true },
                size: { value: 4, random: true },
                line_linked: { enable: true, distance: 120, color: "#ffffff", opacity: 0.4, width: 1.2 },
                move: { enable: true, speed: 4, direction: "none", random: true, straight: false, out_mode: "out" }
            },
            interactivity: {
                detect_on: "canvas",
                events: { onhover: { enable: true, mode: "grab" }, onclick: { enable: true, mode: "push" }, resize: true },
                modes: { grab: { distance: 150, line_linked: { opacity: 0.6 } }, push: { particles_nb: 4 } }
            },
            retina_detect: true
        });
    } else {
        console.warn("particles.js failed to load. Skipping particle initialization.");
    }
</script>
</body>
</html>