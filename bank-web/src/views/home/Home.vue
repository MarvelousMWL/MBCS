<template>
  <div class="portal">
    <canvas ref="bgCanvas" class="bg-canvas"></canvas>
    <div class="portal-content">
      <header class="hero">
        <div class="hero-badge"><span class="hero-badge-dot"></span>MBCS v1.0 · 银行核心业务系统</div>
        <h1>银行<span class="gradient">核心系统</span></h1>
        <p>集柜面业务、产品工厂、客户管理、账务核心于一体的新一代银行核心业务平台</p>
      </header>

      <div class="stats">
        <div class="stat-item" v-for="s in statList" :key="s.label">
          <div class="stat-num">{{ s.num }}</div>
          <div class="stat-label">{{ s.label }}</div>
        </div>
      </div>

      <h2 class="section-title">快捷入口</h2>
      <div class="system-grid">
        <router-link to="/institution" class="system-card" @mousemove="onCardMove" @mouseleave="onCardLeave">
          <div class="card-icon" style="background:rgba(79,110,247,.15);color:#6d8cff">
            <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/></svg>
          </div>
          <h3>机构管理</h3>
          <p>银行机构信息维护与查询</p>
          <span class="card-arrow">→</span>
        </router-link>

        <router-link to="/customer" class="system-card" @mousemove="onCardMove" @mouseleave="onCardLeave">
          <div class="card-icon" style="background:rgba(0,212,170,.15);color:#00d4aa">
            <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
          </div>
          <h3>客户管理</h3>
          <p>客户信息维护、查询与管理</p>
          <span class="card-arrow">→</span>
        </router-link>

        <router-link to="/account/query" class="system-card" @mousemove="onCardMove" @mouseleave="onCardLeave">
          <div class="card-icon" style="background:rgba(240,160,80,.15);color:#f0a050">
            <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="1" y="4" width="22" height="16" rx="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg>
          </div>
          <h3>账户业务</h3>
          <p>开户、销户、冻结、解冻等账户管理</p>
          <span class="card-arrow">→</span>
        </router-link>

        <router-link to="/transaction/deposit" class="system-card" @mousemove="onCardMove" @mouseleave="onCardLeave">
          <div class="card-icon" style="background:rgba(200,120,255,.15);color:#c878ff">
            <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="1.5"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg>
          </div>
          <h3>交易管理</h3>
          <p>存款、取款、转账等核心交易</p>
          <span class="card-arrow">→</span>
        </router-link>

        <router-link to="/product-factory" class="system-card" @mousemove="onCardMove" @mouseleave="onCardLeave">
          <div class="card-icon" style="background:rgba(255,100,130,.15);color:#ff6482">
            <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="3" width="18" height="18" rx="2"/><path d="M3 9h18"/><path d="M9 21V9"/></svg>
          </div>
          <h3>产品工厂</h3>
          <p>金融产品定义、配置与复刻</p>
          <span class="card-arrow">→</span>
        </router-link>

        <router-link to="/teller" class="system-card" @mousemove="onCardMove" @mouseleave="onCardLeave">
          <div class="card-icon" style="background:rgba(100,200,255,.15);color:#64c8ff">
            <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="2" y="3" width="20" height="14" rx="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/></svg>
          </div>
          <h3>柜员管理</h3>
          <p>柜员信息管理与权限配置</p>
          <span class="card-arrow">→</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const bgCanvas = ref(null)
let animId = null, particles = [], w, h

const statList = [
  { num: '6', label: '核心模块' },
  { num: '20+', label: '业务功能' },
  { num: '99.9%', label: '系统可用性' },
]

class Particle {
  constructor() { this.reset() }
  reset() {
    this.x = Math.random() * w; this.y = Math.random() * h
    this.size = Math.random() * 2 + 1
    this.speedX = (Math.random() - .5) * .6
    this.speedY = (Math.random() - .5) * .6
    this.opacity = Math.random() * .4 + .1
  }
  update() {
    this.x += this.speedX; this.y += this.speedY
    if (this.x < 0 || this.x > w || this.y < 0 || this.y > h) this.reset()
  }
  draw(ctx) {
    ctx.beginPath(); ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2)
    ctx.fillStyle = `rgba(109,140,255,${this.opacity})`; ctx.fill()
  }
}

function initParticles() {
  const canvas = bgCanvas.value
  if (!canvas) return
  const ctx = canvas.getContext('2d')
  w = canvas.width = window.innerWidth; h = canvas.height = window.innerHeight
  particles = []
  for (let i = 0; i < 80; i++) particles.push(new Particle())

  function animate() {
    ctx.clearRect(0, 0, w, h)
    for (let i = 0; i < particles.length; i++) {
      for (let j = i + 1; j < particles.length; j++) {
        const dx = particles[i].x - particles[j].x, dy = particles[i].y - particles[j].y
        const dist = Math.sqrt(dx * dx + dy * dy)
        if (dist < 120) {
          ctx.beginPath(); ctx.moveTo(particles[i].x, particles[i].y)
          ctx.lineTo(particles[j].x, particles[j].y)
          ctx.strokeStyle = `rgba(109,140,255,${.06 * (1 - dist / 120)})`
          ctx.lineWidth = .5; ctx.stroke()
        }
      }
    }
    particles.forEach(p => { p.update(); p.draw(ctx) })
    animId = requestAnimationFrame(animate)
  }
  animate()
}

function onResize() {
  const canvas = bgCanvas.value
  if (canvas) { w = canvas.width = window.innerWidth; h = canvas.height = window.innerHeight }
}

function onCardMove(e) {
  const r = e.currentTarget.getBoundingClientRect()
  e.currentTarget.style.setProperty('--mouse-x', (e.clientX - r.left) + 'px')
  e.currentTarget.style.setProperty('--mouse-y', (e.clientY - r.top) + 'px')
}
function onCardLeave(e) {
  e.currentTarget.style.removeProperty('--mouse-x')
  e.currentTarget.style.removeProperty('--mouse-y')
}

onMounted(() => { initParticles(); window.addEventListener('resize', onResize) })
onUnmounted(() => { cancelAnimationFrame(animId); window.removeEventListener('resize', onResize) })
</script>

<style scoped>
*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0 }
.portal {
  position: relative; min-height: calc(100vh - 128px);
  background: #0a0e27; overflow: hidden; color: #e8ecf4;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC", "Microsoft YaHei", sans-serif;
  -webkit-font-smoothing: antialiased;
}
.bg-canvas { position: absolute; inset: 0; z-index: 0 }
.portal-content { position: relative; z-index: 1; max-width: 1200px; margin: 0 auto; padding: 60px 24px 80px }

.hero { text-align: center; padding-bottom: 50px }
.hero-badge {
  display: inline-flex; align-items: center; gap: 8px;
  background: rgba(255,255,255,.06); border: 1px solid rgba(255,255,255,.08);
  border-radius: 100px; padding: 6px 20px; font-size: 13px; color: #8892b0;
  margin-bottom: 24px; backdrop-filter: blur(10px);
}
.hero-badge-dot { width: 7px; height: 7px; border-radius: 50%; background: #00d4aa; animation: pulse 2s infinite }
@keyframes pulse { 0%, 100% { opacity: 1 } 50% { opacity: .4 } }
.hero h1 { font-size: clamp(36px, 6vw, 56px); font-weight: 800; line-height: 1.15; letter-spacing: -1px; margin-bottom: 16px }
.hero h1 .gradient { background: linear-gradient(135deg, #6d8cff 0%, #00d4aa 50%, #f0a050 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text }
.hero p { font-size: 17px; color: #8892b0; max-width: 520px; margin: 0 auto }

.stats { display: flex; justify-content: center; gap: 48px; margin-bottom: 60px; flex-wrap: wrap }
.stat-item { text-align: center }
.stat-num { font-size: 36px; font-weight: 800; background: linear-gradient(135deg, #6d8cff, #00d4aa); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text }
.stat-label { font-size: 13px; color: #8892b0; margin-top: 4px }

.section-title { text-align: center; font-size: 28px; font-weight: 700; margin-bottom: 36px }

.system-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px }
.system-card {
  position: relative; background: rgba(255,255,255,.04); border: 1px solid rgba(255,255,255,.08);
  border-radius: 18px; padding: 32px 24px;
  cursor: pointer; transition: all .35s cubic-bezier(.4, 0, .2, 1);
  overflow: hidden; text-decoration: none !important; color: inherit; display: block;
  backdrop-filter: blur(10px);
  --mouse-x: 50%; --mouse-y: 50%;
}
.system-card::before {
  content: ''; position: absolute; inset: 0;
  background: radial-gradient(400px circle at var(--mouse-x, 50%) var(--mouse-y, 50%), rgba(79, 110, 247, .1), transparent 40%);
  opacity: 0; transition: opacity .4s
}
.system-card:hover::before { opacity: 1 }
.system-card:hover { transform: translateY(-5px); border-color: rgba(79, 110, 247, .3); box-shadow: 0 16px 48px rgba(0, 0, 0, .3) }
.card-icon { width: 52px; height: 52px; border-radius: 14px; display: flex; align-items: center; justify-content: center; margin-bottom: 16px; font-size: 24px }
.system-card h3 { font-size: 19px; font-weight: 700; margin-bottom: 6px }
.system-card p { font-size: 13px; color: #8892b0; line-height: 1.6 }
.card-arrow { position: absolute; right: 24px; top: 50%; transform: translateY(-50%) translateX(6px); font-size: 18px; color: #8892b0; opacity: 0; transition: all .3s }
.system-card:hover .card-arrow { opacity: 1; transform: translateY(-50%) translateX(0) }

@media(max-width: 900px) { .system-grid { grid-template-columns: repeat(2, 1fr) } }
@media(max-width: 600px) { .system-grid { grid-template-columns: 1fr } .stats { gap: 24px } }
</style>
