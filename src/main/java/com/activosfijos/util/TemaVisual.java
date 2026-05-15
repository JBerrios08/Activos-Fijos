package com.activosfijos.util;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public final class TemaVisual {
    public static final Color AZUL_INSTITUCIONAL = Color.decode("#173864");
    public static final Color GRIS_CLARO = Color.decode("#F2F2F2");
    public static final Color AZUL_SUAVE = Color.decode("#B4C7D9");
    public static final Color FONDO_OSCURO = Color.decode("#0D1B2A");
    public static final Color PANEL_OSCURO = Color.decode("#112233");
    public static final Color PANEL_OSCURO_SECUNDARIO = Color.decode("#1A2D40");

    private TemaVisual() {
    }

    public static void aplicarTema(boolean modoOscuro) {
        try {
            if (modoOscuro) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                aplicarPaletaOscura();
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
                aplicarPaletaClara();
            }
        } catch (Exception ex) {
            throw new IllegalStateException("No fue posible aplicar el tema", ex);
        }
    }

    private static void aplicarPaletaClara() {
        UIManager.put("Panel.background", GRIS_CLARO);
        UIManager.put("Viewport.background", GRIS_CLARO);
        UIManager.put("Label.foreground", AZUL_INSTITUCIONAL);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", AZUL_INSTITUCIONAL);
        UIManager.put("TableHeader.background", AZUL_INSTITUCIONAL);
        UIManager.put("TableHeader.foreground", GRIS_CLARO);
        UIManager.put("Button.background", AZUL_SUAVE);
        UIManager.put("Button.foreground", AZUL_INSTITUCIONAL);
        UIManager.put("Button.default.background", AZUL_INSTITUCIONAL);
        UIManager.put("Button.default.foreground", GRIS_CLARO);
        UIManager.put("Component.borderColor", AZUL_SUAVE);
        UIManager.put("Separator.foreground", AZUL_SUAVE);
    }

    private static void aplicarPaletaOscura() {
        UIManager.put("Panel.background", FONDO_OSCURO);
        UIManager.put("Viewport.background", FONDO_OSCURO);
        UIManager.put("Label.foreground", GRIS_CLARO);
        UIManager.put("Table.background", PANEL_OSCURO);
        UIManager.put("Table.foreground", GRIS_CLARO);
        UIManager.put("TableHeader.background", PANEL_OSCURO_SECUNDARIO);
        UIManager.put("TableHeader.foreground", GRIS_CLARO);
        UIManager.put("Button.background", AZUL_SUAVE);
        UIManager.put("Button.foreground", FONDO_OSCURO);
        UIManager.put("Button.default.background", AZUL_INSTITUCIONAL);
        UIManager.put("Button.default.foreground", GRIS_CLARO);
        UIManager.put("Component.borderColor", new Color(180, 199, 217, 140));
        UIManager.put("Separator.foreground", new Color(180, 199, 217, 120));
    }
}
