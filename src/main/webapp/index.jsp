<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HR Help Desk - Welcome</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/particles.js@2.0.0/particles.min.js"></script>
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            animation: gradientShift 8s ease-in-out infinite;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            overflow-x: hidden;
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

        .fade-in-up {
            opacity: 0;
            transform: translateY(30px);
            animation: fadeInUp 1s ease-out forwards;
        }

        .fade-in-up-delay-1 { animation-delay: 0.2s; }
        .fade-in-up-delay-2 { animation-delay: 0.4s; }
        .fade-in-up-delay-3 { animation-delay: 0.6s; }
        .fade-in-up-delay-4 { animation-delay: 0.8s; }

        @keyframes fadeInUp {
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .pulse-glow {
            animation: pulseGlow 2s ease-in-out infinite;
        }

        @keyframes pulseGlow {
            0%, 100% { box-shadow: 0 0 20px rgba(255, 255, 255, 0.3); }
            50% { box-shadow: 0 0 40px rgba(255, 255, 255, 0.6); }
        }

        .float {
            animation: float 3s ease-in-out infinite;
        }

        @keyframes float {
            0%, 100% { transform: translateY(0px); }
            50% { transform: translateY(-10px); }
        }

        .glass-effect {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.2);
        }

        .hover-scale {
            transition: transform 0.3s ease;
        }

        .hover-scale:hover {
            transform: scale(1.05);
        }

        .typing-animation {
            overflow: hidden;
            border-right: 3px solid rgba(255, 255, 255, 0.75);
            white-space: nowrap;
            animation: typing 3s steps(40, end), blink-caret 0.75s step-end infinite;
        }

        @keyframes typing {
            from { width: 0; }
            to { width: 100%; }
        }

        @keyframes blink-caret {
            from, to { border-color: transparent; }
            50% { border-color: rgba(255, 255, 255, 0.75); }
        }

        .container {
            text-align: center;
            padding: 40px;
            max-width: 1000px;
            position: relative;
            z-index: 2;
        }
    </style>
</head>
<body>
<div id="particles-js"></div>

<!-- Main Content -->
<div class="container text-center z-10 max-w-4xl mx-auto px-6">


    <!-- Main Heading -->
    <h1 class="fade-in-up fade-in-up-delay-1 text-6xl md:text-7xl font-light text-white mb-6">
        HR Help Desk
    </h1>

    <!-- Animated Subtitle -->
    <div class="fade-in-up fade-in-up-delay-2 mb-8">
        <p class="text-xl md:text-2xl text-white/80 typing-animation inline-block">
            Because you deserve nothing less than excellence
        </p>
    </div>

    <!-- Description -->
    <p class="fade-in-up fade-in-up-delay-3 text-lg text-white/70 mb-12 max-w-2xl mx-auto leading-relaxed">
        Welcome to your HR support hub! Easily resolve queries, access resources, and empower your team with our vibrant, user-friendly platform.
    </p>

    <!-- CTA Button -->
    <div class="fade-in-up fade-in-up-delay-4">
        <a href="jsp/login.jsp" class="glass-effect hover-scale px-12 py-4 rounded-full text-white font-medium text-lg transition-all duration-300 hover:bg-white/20 pulse-glow" id="login-btn">
            Login to HR Help Desk
            <span class="ml-2">→</span>
        </a>
    </div>

    <!-- Feature Highlights -->
    <div class="fade-in-up fade-in-up-delay-4 mt-16 grid md:grid-cols-3 gap-8">
        <div class="glass-effect rounded-2xl p-6 hover-scale">
            <div class="text-3xl mb-3">⚡</div>
            <h3 class="text-white font-semibold mb-2">Instant Solutions</h3>
            <p class="text-white/60 text-sm">Resolve HR queries in a flash.</p>
        </div>
        <div class="glass-effect rounded-2xl p-6 hover-scale">
            <div class="text-3xl mb-3">🌐</div>
            <h3 class="text-white font-semibold mb-2">Global Access</h3>
            <p class="text-white/60 text-sm">Support anytime, anywhere.</p>
        </div>
        <div class="glass-effect rounded-2xl p-6 hover-scale">
            <div class="text-3xl mb-3">🔒</div>
            <h3 class="text-white font-semibold mb-2">Fortified Security</h3>
            <p class="text-white/60 text-sm">Your data, safe and secure.</p>
        </div>
    </div>

    <!-- Footer -->
    <footer class="fade-in-up fade-in-up-delay-4 mt-12">
        <p class="text-white/60 text-sm">&copy; <%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()) %> HR Help Desk. By Batch02_Group_39_SKU.</p>
    </footer>
</div>

<!-- Subtle particles effect -->
<div class="absolute inset-0 pointer-events-none">
    <div class="particle absolute w-1 h-1 bg-white/30 rounded-full" style="top: 20%; left: 10%; animation: float 4s ease-in-out infinite;"></div>
    <div class="particle absolute w-1 h-1 bg-white/30 rounded-full" style="top: 60%; left: 80%; animation: float 5s ease-in-out infinite 1s;"></div>
    <div class="particle absolute w-1 h-1 bg-white/30 rounded-full" style="top: 80%; left: 20%; animation: float 6s ease-in-out infinite 2s;"></div>
    <div class="particle absolute w-1 h-1 bg-white/30 rounded-full" style="top: 30%; left: 70%; animation: float 4.5s ease-in-out infinite 0.5s;"></div>
</div>

<script>
    // Initialize particles on page load
    function initParticles() {
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
    }

    // Run on page load
    window.addEventListener('load', initParticles);

    // Add sparkle effect on mouse move
    document.addEventListener('mousemove', (e) => {
        if (Math.random() > 0.98) {
            createSparkle(e.clientX, e.clientY);
        }
    });

    function createSparkle(x, y) {
        const sparkle = document.createElement('div');
        sparkle.className = 'absolute w-2 h-2 bg-white rounded-full pointer-events-none';
        sparkle.style.left = x + 'px';
        sparkle.style.top = y + 'px';
        sparkle.style.animation = 'sparkle 1s ease-out forwards';
        document.body.appendChild(sparkle);

        setTimeout(() => sparkle.remove(), 1000);
    }

    // Add sparkle animation
    const style = document.createElement('style');
    style.textContent = `
            @keyframes sparkle {
                0% { opacity: 1; transform: scale(0); }
                50% { opacity: 1; transform: scale(1); }
                100% { opacity: 0; transform: scale(0); }
            }
        `;
    document.head.appendChild(style);

    // Transition effect for login button
    document.getElementById('login-btn').addEventListener('click', function(e) {
        e.preventDefault();
        const href = this.href;
        document.body.style.transition = 'opacity 0.5s ease-out';
        document.body.style.opacity = '0';

        setTimeout(() => {
            window.location.href = href;
        }, 500);
    });
</script>
</body>
</html>