import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SideBar } from '../../components/side-bar/side-bar';
import { ToastHost } from '../../components/toasts/toast-host/toast-host';

@Component({
  selector: 'app-main-layout',
  imports: [RouterOutlet, SideBar, ToastHost],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout {}
