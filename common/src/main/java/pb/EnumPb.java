// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Enum.proto

package pb;

public final class EnumPb {
  private EnumPb() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  /**
   * Protobuf enum {@code DeviceType}
   */
  public enum DeviceType
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <pre>
     * 未知
     * </pre>
     *
     * <code>UNKNOWN_DEVICE_TYPE = 0;</code>
     */
    UNKNOWN_DEVICE_TYPE(0),
    /**
     * <pre>
     * 安卓
     * </pre>
     *
     * <code>ANDROID = 1;</code>
     */
    ANDROID(1),
    /**
     * <pre>
     * 苹果
     * </pre>
     *
     * <code>IOS = 2;</code>
     */
    IOS(2),
    /**
     * <pre>
     * win
     * </pre>
     *
     * <code>WIN_PC = 3;</code>
     */
    WIN_PC(3),
    /**
     * <pre>
     * mac
     * </pre>
     *
     * <code>MAC_PC = 4;</code>
     */
    MAC_PC(4),
    /**
     * <pre>
     * web
     * </pre>
     *
     * <code>WEB = 5;</code>
     */
    WEB(5),
    UNRECOGNIZED(-1),
    ;

    /**
     * <pre>
     * 未知
     * </pre>
     *
     * <code>UNKNOWN_DEVICE_TYPE = 0;</code>
     */
    public static final int UNKNOWN_DEVICE_TYPE_VALUE = 0;
    /**
     * <pre>
     * 安卓
     * </pre>
     *
     * <code>ANDROID = 1;</code>
     */
    public static final int ANDROID_VALUE = 1;
    /**
     * <pre>
     * 苹果
     * </pre>
     *
     * <code>IOS = 2;</code>
     */
    public static final int IOS_VALUE = 2;
    /**
     * <pre>
     * win
     * </pre>
     *
     * <code>WIN_PC = 3;</code>
     */
    public static final int WIN_PC_VALUE = 3;
    /**
     * <pre>
     * mac
     * </pre>
     *
     * <code>MAC_PC = 4;</code>
     */
    public static final int MAC_PC_VALUE = 4;
    /**
     * <pre>
     * web
     * </pre>
     *
     * <code>WEB = 5;</code>
     */
    public static final int WEB_VALUE = 5;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static DeviceType valueOf(int value) {
      return forNumber(value);
    }

    public static DeviceType forNumber(int value) {
      switch (value) {
        case 0: return UNKNOWN_DEVICE_TYPE;
        case 1: return ANDROID;
        case 2: return IOS;
        case 3: return WIN_PC;
        case 4: return MAC_PC;
        case 5: return WEB;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<DeviceType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        DeviceType> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<DeviceType>() {
            public DeviceType findValueByNumber(int number) {
              return DeviceType.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return pb.EnumPb.getDescriptor().getEnumTypes().get(0);
    }

    private static final DeviceType[] VALUES = values();

    public static DeviceType valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private DeviceType(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:DeviceType)
  }

  /**
   * Protobuf enum {@code ResultState}
   */
  public enum ResultState
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <pre>
     * 成功
     * </pre>
     *
     * <code>SUCCESS = 0;</code>
     */
    SUCCESS(0),
    /**
     * <pre>
     * 失败
     * </pre>
     *
     * <code>FAIL = 1;</code>
     */
    FAIL(1),
    UNRECOGNIZED(-1),
    ;

    /**
     * <pre>
     * 成功
     * </pre>
     *
     * <code>SUCCESS = 0;</code>
     */
    public static final int SUCCESS_VALUE = 0;
    /**
     * <pre>
     * 失败
     * </pre>
     *
     * <code>FAIL = 1;</code>
     */
    public static final int FAIL_VALUE = 1;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static ResultState valueOf(int value) {
      return forNumber(value);
    }

    public static ResultState forNumber(int value) {
      switch (value) {
        case 0: return SUCCESS;
        case 1: return FAIL;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<ResultState>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        ResultState> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<ResultState>() {
            public ResultState findValueByNumber(int number) {
              return ResultState.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return pb.EnumPb.getDescriptor().getEnumTypes().get(1);
    }

    private static final ResultState[] VALUES = values();

    public static ResultState valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private ResultState(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:ResultState)
  }


  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\nEnum.proto*\\\n\nDeviceType\022\027\n\023UNKNOWN_DE" +
      "VICE_TYPE\020\000\022\013\n\007ANDROID\020\001\022\007\n\003IOS\020\002\022\n\n\006WIN" +
      "_PC\020\003\022\n\n\006MAC_PC\020\004\022\007\n\003WEB\020\005*$\n\013ResultStat" +
      "e\022\013\n\007SUCCESS\020\000\022\010\n\004FAIL\020\001B\014\n\002pbB\006EnumPbb\006" +
      "proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
