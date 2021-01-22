<template>
  <div>
    <div class="row">
      <div class="items col-md-7">
        <input type="text" class="form-control" v-model="filter" placeholder="O que você está procurando?">
        <br/>
        <div class="list-group">
          <div class="list-group-item" v-for="item in filteredItems" v-bind:key="item.id">
            <div class="row">
              <div class="col-md-2">
                <span class="badge badge-info">{{ item.category }}</span>
              </div>
              <div class="col-md-6">
                {{ item.description }}
              </div>
              <div class="col-md-2">
                <span class="badge badge-success">{{ formatMoney(item.price) }}</span>
              </div>
              <div class="col-md-2 text-right">
                <button class="btn btn-outline-info btn-sm" v-on:click="addItem(item)">
                <span class="fa fa-plus"></span>
                </button>
                &nbsp;
                <button class="btn btn-outline-info btn-sm" v-on:click="deleteItem(item)">
                <span class="fa fa-minus"></span>
                </button>
              </div>
            </div>
          </div>
        </div>
        <hr/>
        <div class="form-group"> 
          <input type="text" class="form-control" v-model="item.category" placeholder="Categoria">
        </div>
        <div class="form-group">
          <input type="text" class="form-control" v-model="item.description" placeholder="Descrição">
        </div>
        <div class="form-group">
          <input type="text" class="form-control" v-model.number="item.price" placeholder="Preço">
        </div>
        <button class="btn btn-primary btn-block" type="submit" @click="submit()">Adicionar</button>
      </div>
      <div class="order col-md-5">
        <div class="row">
          <div class="col-md">
            <h4>Pedido #{{ order.id }}</h4>
          </div>
          <div class="col-md text-right">
            <h4>Pedido #{{ formatMoney(order.total) }}</h4>
          </div>
        </div>
        <hr/>
        <div v-for="orderItem in order.orderItems" v-bind:key="orderItem.item.id">
          <div class="row">
            <div class="col-md-2">
              {{ orderItem.quantity }}
            </div>
            <div class="col-md-7">
              {{ orderItem.item.description }}
            </div>
            <div class="col-md-3">
              {{ formatMoney(orderItem.quantity * orderItem.item.price) }}
            </div>
          </div>
          <hr/>
        </div>
        <h5>Forma de pagamento: </h5>
      <div class="btn-group btn-group-toggle" data-toggle="buttons">
        <label class="btn btn-secondary">
         <button class="btn btn-primary btn-block" type="submit" @click="comprarComCartao()">Dinheiro</button>
        </label>
        <label class="btn btn-secondary">
           <button class="btn btn-primary btn-block" type="submit" @click="comprarComCartao()">Cartão</button>
        </label>
      </div>
      <hr>
      <input type="text" class="form-control" v-model="valorRecebido" placeholder="Valor recebido">
      <hr/>
      <input type="text" class="form-control" v-model="troco" placeholder="Troco" disabled>
      <hr>
      </div>
    </div>
  </div>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.css'
import 'font-awesome/css/font-awesome.css'
import axios from 'axios/dist/axios'  

export default {
  name: 'App',
  data() {
    return {
      items: [],
      item: [],
      filter: "",
      order: {
        id: Math.floor(Math.random() * 10000),
        orderItems: [],
        total: 0
      },
      valorRecebido: null,
      troco: "",
    }
  },
  methods: {
    formatMoney(value) {
      const formatter = new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
      });
      return formatter.format(value);
    },
    addItem(item) {
      const existingItem = this.order.orderItems.find(orderItem => orderItem.item.id === item.id)
      if (!existingItem) {
       this.order.orderItems.push({ quantity: 1, item })
      } else {
        existingItem.quantity++
      }
      this.order.total += item.price
    },
    deleteItem(item) {
      const existingItem = this.order.orderItems.find(orderItem => orderItem.item.id === item.id)
      if (existingItem) {
        if (existingItem.quantity > 0) existingItem.quantity--
        if (existingItem.quantity === 0) {
          this.order.orderItems.splice(this.order.orderItems.indexOf(existingItem), 1)
        }
        if (this.order.total > 0 && this.order.total >= item.price) this.order.total -= item.price
      }
    },
    comprarComCartao() { 
      this.troco = this.order.total
    },
    // saveItem(item) {
    //   const identifier = Math.floor(Math.random() * 10000)
    //   this.items.push({ id: identifier, category: item.category, description: item.description, price: item.price })
    // },
    submit() {
      axios
        .post("http://localhost:8081/cadastrarItem", {
          category: this.item.category,
          description: this.item.description,
          price: this.item.price
        })
        .then(response => {
          this.items.push(response.data)
          this.item.category = ""
          this.item.description = ""
          this.item.price = ""
        })
        .catch(error => {
          console.error("There was an error!", error);
        });
    }
  },
  computed: {
    filteredItems() {
      if (!this.filter) return this.items
      return this.items.filter(item => item.description.toLowerCase().includes(this.filter.toLowerCase()))
    },
  },
  created() {
    axios.get("http://localhost:8081/items").then(res => {
      console.log(res.data)
      this.items = res.data
    });
    // this.items.push({ id: 1, category: 'Bebida', description: 'Água', price: 2 })
    // this.items.push({ id: 2, category: 'Comida', description: 'Churrasco', price: 30 })
    // this.items.push({ id: 3, category: 'Sobremesa', description: 'Pudim', price: 30 })
  }
};
</script>

<style>
.items {
  padding: 30px;
  height: 100vh;
}
.order {
  padding: 30px;
  background-color: #F1F1F1;
}
</style>
