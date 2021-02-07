<template>
  <nav aria-label="Navegação de página exemplo">
    <ul class="pagination">
      <li class="page-item" :class="{ disabled: source.number == 0 }">
        <a class="page-link" href="#" @click="navegacao($event,
         source.number)" aria-label="Anterior">
          <span aria-hidden="true">&laquo;</span>
          <span class="sr-only">Anterior</span>
        </a>
      </li>
      <li v-for="page in pages" :class="{ active: source.number+1 == page }" 
      v-bind:key="page.id" class="page-item">
        <a class="page-link" href="#" @click="navegacao($event, page)">{{ page }}</a>
      </li>
      <li class="page-item" :class="{ disabled: source.last == true }">
        <a class="page-link" href="#" @click="navegacao($event,
         source.number+2)" aria-label="Próximo">
          <span aria-hidden="true">&raquo;</span>
          <span class="sr-only">Próximo</span>
        </a>
      </li>
    </ul>
  </nav>
</template>

<script>
  import { range } from 'lodash' 
  export default {
    props: ['source'],
    data () {
      return {
        pages: []
      }
    },
    methods: {
      navegacao(ev, page) {
        ev.preventDefault()
        this.$emit('navegacao', page)
      }
    },
    watch: {
      source() {
        this.pages = range(1, this.source.totalPages+1)
      }
    }
  }
</script>