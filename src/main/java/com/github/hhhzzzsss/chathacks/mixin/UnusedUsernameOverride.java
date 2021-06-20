package com.github.hhhzzzsss.chathacks.mixin;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.github.hhhzzzsss.chathacks.ChatHacks;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkState;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.logging.UncaughtExceptionLogger;

@Mixin(ConnectScreen.class)
public abstract class UnusedUsernameOverride {
	
	/*@Shadow
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Shadow
	private static final AtomicInteger CONNECTOR_THREADS_COUNT = new AtomicInteger(0);
	
	@Shadow
	private ClientConnection connection;
	
	@Shadow
	private boolean connectingCancelled;
	
	@Shadow
	private final Screen parent;
	
	@Shadow
	private void setStatus(Text text) {}
	
	public UsernameOverride(Screen parent) {
		this.parent = parent;
	}
	
	@Overwrite
	private void connect(final String address, final int port) {
		  System.out.println("test");
	      LOGGER.info("Connecting to {}, {}", address, port);
	      Thread thread = new Thread("Server Connector #" + CONNECTOR_THREADS_COUNT.incrementAndGet()) {
	         public void run() {
	            InetAddress inetAddress = null;

	            try {
	               if (connectingCancelled) {
	                  return;
	               }

	               inetAddress = InetAddress.getByName(address);
	               connection = ClientConnection.connect(inetAddress, port, MinecraftClient.getInstance().options.shouldUseNativeTransport());
	               connection.setPacketListener(new ClientLoginNetworkHandler(connection, MinecraftClient.getInstance(), parent, (text) -> {
	                  setStatus(text);
	               }));
	               connection.send(new HandshakeC2SPacket(address, port, NetworkState.LOGIN));
	               connection.send(new LoginHelloC2SPacket(new GameProfile(MinecraftClient.getInstance().getSession().getProfile().getId(), ChatHacks.username)));
	            } catch (UnknownHostException var4) {
	               if (connectingCancelled) {
	                  return;
	               }

	               LOGGER.error("Couldn't connect to server", var4);
	               MinecraftClient.getInstance().execute(() -> {
	            	   MinecraftClient.getInstance().openScreen(new DisconnectedScreen(parent, "connect.failed", new TranslatableText("disconnect.genericReason", new Object[]{"Unknown host"})));
	               });
	            } catch (Exception var5) {
	               if (connectingCancelled) {
	                  return;
	               }

	               LOGGER.error("Couldn't connect to server", var5);
	               String string = inetAddress == null ? var5.toString() : var5.toString().replaceAll(inetAddress + ":" + port, "");
	               MinecraftClient.getInstance().execute(() -> {
	            	   MinecraftClient.getInstance().openScreen(new DisconnectedScreen(parent, "connect.failed", new TranslatableText("disconnect.genericReason", new Object[]{string})));
	               });
	            }
	         }
	      };
	      thread.setUncaughtExceptionHandler(new UncaughtExceptionLogger(LOGGER));
	      thread.start();
	}*/
}
