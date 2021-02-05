<template>
  <div style="overflow:hidden;">
    <div class="row">
      <div class="items col-md-7">
        <input type="text" class="form-control" v-model="filter" placeholder="O que você está procurando?">
        <br/>
        <div class="list-group">
          <div class="list-group-item">
            <div class="row">
              <div class="col-md-2">
                <span class="font-weight-bold text-uppercase">Categoria</span>
              </div>
              <div class="col-md-4">
                <span class="font-weight-bold text-uppercase">Descrição</span>
              </div>
              <div class="col-md-2">
                <span class="font-weight-bold text-uppercase">Preço</span>
              </div>
              <div class="col-md-2">
                <span class="font-weight-bold text-uppercase">Editar</span>
              </div>
            </div>
          </div>
        </div>
        <div class="list-group">
          <div class="list-group-item" v-for="item in filteredItems" v-bind:key="item.id">
            <div class="row">
              <div class="col-md-2">
                <span href="#" class="badge badge-info">{{ item.category }}</span>
              </div>
              <div class="col-md-4">
                {{ item.description }}
              </div>
              <div class="col-md-2">
                <span class="badge badge-success">{{ formatMoney(item.price) }}</span>
              </div>
               <div class="col-md-2">
                <button class="btn btn-outline-danger btn-sm" v-on:click="editarItem(item)">
                  <span class="fa fa-edit"></span>
                </button>
                &nbsp;
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
            <h4>Total #{{ formatMoney(order.total) }}</h4>
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
        <h5>Formas de pagamento: </h5>
        <button type="submit" @click="comprarComCartao()" class="btn btn-outline-secondary">Cartão</button> 
      <hr>
      <div class="input-group">
        <input type="text" class="form-control" v-model="order.valorRecebido" placeholder="Valor a receber">
        <div class="input-group-append">
          <button type="submit" @click="comprarComDinheiro(order.valorRecebido)" class="btn btn-outline-success">Dinheiro</button>
        </div>
      </div>
      <hr/>
       <div class="col-md text-right">
        <h5>Troco: {{ formatMoney(order.troco) }}</h5>
      </div>
      </div>
    </div>
  </div>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.css'
import 'font-awesome/css/font-awesome.css'
import axios from 'axios/dist/axios'
import swal from 'sweetalert';

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
        valorRecebido: "",
        troco: 0,
        total: 0
      },
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
    editarItem(item) {
      this.item = item;
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
      swal("Ótimo", "Sua compra com cartão foi concluída com sucesso!", "success")
      this.order.orderItems = []
      this.order.troco = ""
      this.order.total = 0
    },
    comprarComDinheiro(valor) {
      if (valor >= this.order.total) {
        this.order.troco = Math.abs(this.order.total - valor)
        this.order.valorRecebido = ""
        this.order.orderItems = []
        this.order.total = 0
        swal("Ótimo", "Sua compra foi concluída com sucesso!", "success")
      } else {
        this.order.valorRecebido = ""
        swal("Atenção", "Valor informado é insuficiente!", "warning")
      }
    },
    submit() {
      if(!this.item.id) {
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
      } else {
        axios.put("http://localhost:8081/atualizarItem", {
          id: this.item.id,
          category: this.item.category,
          description: this.item.description,
          price: this.item.price
        })
        .then(response => {
          console.log(response.data)
          swal("Ótimo", "Sua compra foi atualizada com sucesso!", "success")
        })
         .catch(error => {
          console.error("There was an error!", error);
        });
      }
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
      this.items = res.data
    });
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
