import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TerminalPonto } from './terminal-ponto';

describe('TerminalPonto', () => {
  let component: TerminalPonto;
  let fixture: ComponentFixture<TerminalPonto>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TerminalPonto],
    }).compileComponents();

    fixture = TestBed.createComponent(TerminalPonto);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
